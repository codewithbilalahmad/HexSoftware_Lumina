package com.muhammad.lumina.data

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.database.ContentObserver
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.RectF
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import com.muhammad.lumina.domain.model.EditedPhoto
import com.muhammad.lumina.domain.repository.ImageUtilsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.OutputStream
import kotlin.math.roundToInt

class ImageUtilsRepositoryImp(
    private val context: Context,
    private val scope : CoroutineScope
) : ImageUtilsRepository {
    private val _editedPhotos = MutableStateFlow<List<EditedPhoto>>(emptyList())
    override val editedPhotos = _editedPhotos.asStateFlow()
    private val contentObserver = object : ContentObserver(null) {
        override fun onChange(selfChange: Boolean) {
            super.onChange(selfChange)
            loadEditedPhotos()
        }
    }

    init {
        val imageUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        } else {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }
        context.contentResolver.registerContentObserver(imageUri, true, contentObserver)
        loadEditedPhotos()
    }

    override fun loadEditedPhotos() {
        scope.launch(Dispatchers.IO){
            _editedPhotos.value = getImagesFromLuminaFolder()
        }
    }

    @SuppressLint("UseKtx")
    override fun applyEditsToBitmap(
        bitmap: Bitmap,
        brightness: Float,
        contrast: Float,
        saturation: Float,
        rotation: Float,
        flipVertical: Boolean,
        flipHorizontal: Boolean,
    ): Bitmap {
        val safeBrightness = brightness.coerceIn(-100f, 100f)
        val safeContrast = contrast.coerceIn(0.2f, 3f)
        val safeSaturation = saturation.coerceIn(0f, 3f)
        val colorMatrix = ColorMatrix()
        val saturationMatrix = ColorMatrix().apply {
            setSaturation(safeSaturation)
        }
        colorMatrix.postConcat(saturationMatrix)

        val translate = 128f * (1f - contrast)
        val contrastArray = floatArrayOf(
            safeContrast, 0f, 0f, 0f, translate,
            0f, safeContrast, 0f, 0f, translate,
            0f, 0f, safeContrast, 0f, translate,
            0f, 0f, 0f, 1f, 0f
        )
        colorMatrix.postConcat(ColorMatrix(contrastArray))

        val bright = safeBrightness * 2.55f
        val brightnessArray = floatArrayOf(
            1f, 0f, 0f, 0f, bright,
            0f, 1f, 0f, 0f, bright,
            0f, 0f, 1f, 0f, bright,
            0f, 0f, 0f, 1f, 0f
        )
        colorMatrix.postConcat(ColorMatrix(brightnessArray))

        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            isFilterBitmap = true
            isDither = true
            colorFilter = ColorMatrixColorFilter(colorMatrix)
        }

        val matrix = Matrix()

        val sx = if (flipHorizontal) -1f else 1f
        val sy = if (flipVertical) -1f else 1f

        matrix.preScale(sx, sy)

        val rot = rotation % 360f
        if (rot != 0f) {
            matrix.postRotate(rot)
        }

        val srcRect = RectF(0f, 0f, bitmap.width.toFloat(), bitmap.height.toFloat())
        val dstRect = RectF()
        matrix.mapRect(dstRect, srcRect)

        val dstWidth = dstRect.width().roundToInt().coerceAtLeast(1)
        val dstHeight = dstRect.height().roundToInt().coerceAtLeast(1)

        val outBitmap = Bitmap.createBitmap(dstWidth, dstHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(outBitmap)

        val drawMatrix = Matrix(matrix).apply {
            postTranslate(-dstRect.left, -dstRect.top)
        }
        canvas.drawBitmap(bitmap, drawMatrix, paint)
        return outBitmap
    }

    override fun saveBitmapToStream(bitmap: Bitmap, out: OutputStream) {
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
        out.flush()
        out.close()
    }

    override fun saveImageInExternalStorage(bitmap: Bitmap): Uri? {
        return try {
            val displayName = "Edtited_${System.currentTimeMillis()}.jpg"
            val contentValues = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, displayName)
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                put(
                    MediaStore.Images.Media.RELATIVE_PATH,
                    "${Environment.DIRECTORY_PICTURES}/Lumina"
                )
                put(MediaStore.Images.Media.IS_PENDING, 1)
            }
            val collection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
            } else {
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            }
            val uri = context.contentResolver.insert(collection, contentValues) ?: return null
            context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                saveBitmapToStream(bitmap = bitmap, out = outputStream)
            }
            contentValues.clear()
            contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
            context.contentResolver.update(uri, contentValues, null, null)
            return uri
        } catch (e: Exception) {
            e.printStackTrace()

            null
        }
    }

    override fun getImagesFromLuminaFolder(): List<EditedPhoto> {
        val editedPhotos = mutableListOf<EditedPhoto>()
        val collection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        } else {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }
        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.RELATIVE_PATH
        )
        val selection = "${MediaStore.Images.Media.RELATIVE_PATH} LIKE ?"
        val selectionArgs = arrayOf("%Lumina/%")
        val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"
        context.contentResolver.query(
            collection, projection, selection, selectionArgs, sortOrder
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val uri = ContentUris.withAppendedId(
                    collection, id
                )
                editedPhotos.add(EditedPhoto(id = id, uri = uri.toString()))
            }
        }
        return editedPhotos
    }
}