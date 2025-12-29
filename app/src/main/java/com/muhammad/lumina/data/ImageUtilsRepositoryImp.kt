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
import androidx.compose.ui.unit.sp
import com.muhammad.lumina.domain.model.EditedPhoto
import com.muhammad.lumina.domain.model.EmojiLayer
import com.muhammad.lumina.domain.model.PhotoFilter
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
    private val scope: CoroutineScope,
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
        scope.launch(Dispatchers.IO) {
            _editedPhotos.value = getImagesFromLuminaFolder()
        }
    }

    private fun getFilterMatrix(filter : PhotoFilter) : ColorMatrix{
        return when(filter){
            PhotoFilter.SEPIA -> ColorMatrix(
                floatArrayOf(
                    0.393f, 0.769f, 0.189f, 0f, 0f,
                    0.349f, 0.686f, 0.168f, 0f, 0f,
                    0.272f, 0.534f, 0.131f, 0f, 0f,
                    0f, 0f, 0f, 1f, 0f
                )
            )
            PhotoFilter.WARM -> ColorMatrix(
                floatArrayOf(
                    1.12f, 0f, 0f, 0f, 10f,
                    0f, 1.04f, 0f, 0f, 6f,
                    0f, 0f, 0.95f, 0f, -6f,
                    0f, 0f, 0f, 1f, 0f
                )
            )
            PhotoFilter.COOL -> ColorMatrix(
                floatArrayOf(
                    0.95f, 0f, 0f, 0f, -6f,
                    0f, 0.98f, 0f, 0f, 0f,
                    0f, 0f, 1.12f, 0f, 10f,
                    0f, 0f, 0f, 1f, 0f
                )
            )
            PhotoFilter.TEAL_ORANGE -> ColorMatrix(
                floatArrayOf(
                    1.15f, -0.05f, -0.05f, 0f, 6f,
                    -0.05f, 1.05f, -0.05f, 0f, 6f,
                    -0.05f, -0.05f, 1.15f, 0f, 10f,
                    0f, 0f, 0f, 1f, 0f
                )
            )
            PhotoFilter.VINTAGE -> ColorMatrix(
                floatArrayOf(
                    0.9f, 0f, 0f, 0f, 12f,
                    0f, 0.85f, 0f, 0f, 8f,
                    0f, 0f, 0.8f, 0f, -6f,
                    0f, 0f, 0f, 1f, 0f
                )
            )
            PhotoFilter.MOODY -> ColorMatrix(
                floatArrayOf(
                    0.8f, 0.02f, 0f, 0f, -20f,
                    0f, 0.9f, 0.02f, 0f, -12f,
                    0f, 0f, 0.95f, 0f, -6f,
                    0f, 0f, 0f, 1f, 0f
                )
            )
            PhotoFilter.BLACK_WHITE -> ColorMatrix(
                floatArrayOf(
                    0.33f, 0.59f, 0.11f, 0f, 0f,
                    0.33f, 0.59f, 0.11f, 0f, 0f,
                    0.33f, 0.59f, 0.11f, 0f, 0f,
                    0f, 0f, 0f, 1f, 0f
                )
            )
            PhotoFilter.CINEMATIC -> ColorMatrix(
                floatArrayOf(
                    1.2f, -0.1f, -0.1f, 0f, 10f,
                    -0.05f, 1.1f, -0.1f, 0f, 5f,
                    -0.1f, -0.05f, 1.3f, 0f, 15f,
                    0f, 0f, 0f, 1f, 0f
                )
            )
            PhotoFilter.FADE_FILM -> ColorMatrix(
                floatArrayOf(
                    0.9f, 0f, 0f, 0f, 12f,
                    0f, 0.9f, 0f, 0f, 12f,
                    0f, 0f, 0.9f, 0f, 12f,
                    0f, 0f, 0f, 1f, 0f
                )
            )
            PhotoFilter.MATTE -> ColorMatrix(
                floatArrayOf(
                    0.85f, 0f, 0f, 0f, 20f,
                    0f, 0.85f, 0f, 0f, 20f,
                    0f, 0f, 0.85f, 0f, 20f,
                    0f, 0f, 0f, 1f, 0f
                )
            )
            PhotoFilter.VIBRANT -> ColorMatrix(
                floatArrayOf(
                    1.15f, 0.05f, 0.05f, 0f, 0f,
                    0.05f, 1.15f, 0.05f, 0f, 0f,
                    0.05f, 0.05f, 1.15f, 0f, 0f,
                    0f,     0f,     0f, 1f, 0f
                )
            )
            PhotoFilter.SOFT_PASTEL -> ColorMatrix(
                floatArrayOf(
                    1.05f, -0.05f, -0.05f, 0f, 10f,
                    -0.05f, 1.05f, -0.05f, 0f, 10f,
                    -0.05f, -0.05f, 1.05f, 0f, 15f,
                    0f,     0f,     0f,   1f, 0f
                )
            )
            PhotoFilter.LUSH_GREEN -> ColorMatrix(
                floatArrayOf(
                    1.0f, -0.05f, -0.05f, 0f, 0f,
                    0f,   1.25f,   0f,    0f, 5f,
                    0f,  -0.05f,  1.05f,  0f, 5f,
                    0f,    0f,     0f,    1f, 0f
                )
            )
            PhotoFilter.SUNNY_GLOW -> ColorMatrix(
                floatArrayOf(
                    1.15f, 0.05f, 0f, 0f, 10f,
                    0.05f, 1.10f, 0f, 0f, 8f,
                    0f,    0f,   0.95f, 0f, 0f,
                    0f, 0f, 0f, 1f, 0f
                )
            )
            PhotoFilter.MONO -> ColorMatrix(
                floatArrayOf(
                    0.33f, 0.33f, 0.33f, 0f, 0f,
                    0.33f, 0.33f, 0.33f, 0f, 0f,
                    0.33f, 0.33f, 0.33f, 0f, 0f,
                    0f,    0f,    0f,    1f, 0f
                )
            )
            PhotoFilter.SOFT_MONO -> ColorMatrix(
                floatArrayOf(
                    0.28f, 0.28f, 0.28f, 0f, 20f,
                    0.28f, 0.28f, 0.28f, 0f, 20f,
                    0.28f, 0.28f, 0.28f, 0f, 20f,
                    0f,    0f,    0f,    1f, 0f
                )
            )
            PhotoFilter.HIGH_CONTRAST_BW -> ColorMatrix(
                floatArrayOf(
                    1.5f * 0.33f, 1.5f * 0.33f, 1.5f * 0.33f, 0f, -20f,
                    1.5f * 0.33f, 1.5f * 0.33f, 1.5f * 0.33f, 0f, -20f,
                    1.5f * 0.33f, 1.5f * 0.33f, 1.5f * 0.33f, 0f, -20f,
                    0f,           0f,           0f,           1f, 0f
                )
            )
            PhotoFilter.FILM_GRAIN_BW -> ColorMatrix(
                floatArrayOf(
                    0.32f, 0.32f, 0.32f, 0f, 12f,
                    0.32f, 0.32f, 0.32f, 0f, 12f,
                    0.32f, 0.32f, 0.32f, 0f, 12f,
                    0f,    0f,    0f,    1f, 0f
                )
            )
            PhotoFilter.RETRO -> ColorMatrix(
                floatArrayOf(
                    1.2f, -0.2f, 0f, 0f, 20f,
                    0f, 1.1f, -0.1f, 0f, 10f,
                    -0.1f, 0.1f, 1.1f, 0f, 5f,
                    0f, 0f, 0f, 1f, 0f
                )
            )

            PhotoFilter.FILTER_1977 -> ColorMatrix(
                floatArrayOf(
                    1.2f, 0f, 0.1f, 0f, 20f,
                    0f, 1.1f, 0.1f, 0f, 10f,
                    0f, 0f, 1.4f, 0f, 15f,
                    0f, 0f, 0f, 1f, 0f
                )
            )

            PhotoFilter.OLD_PAPER -> ColorMatrix(
                floatArrayOf(
                    1f, 0.1f, 0f, 0f, 25f,
                    0.1f, 1f, 0f, 0f, 20f,
                    0f, 0f, 0.8f, 0f, 0f,
                    0f, 0f, 0f, 1f, 0f
                )
            )

            PhotoFilter.WASHED -> ColorMatrix(
                floatArrayOf(
                    0.8f, 0.1f, 0.1f, 0f, 20f,
                    0.1f, 0.8f, 0.1f, 0f, 20f,
                    0.1f, 0.1f, 0.8f, 0f, 20f,
                    0f, 0f, 0f, 1f, 0f
                )
            )

            PhotoFilter.DUOTONE_BLUE_RED -> ColorMatrix(
                floatArrayOf(
                    0.2f, 0.1f, 0.9f, 0f, 0f,
                    0.1f, 0.1f, 0.1f, 0f, 0f,
                    0.9f, 0.1f, 0.2f, 0f, 0f,
                    0f, 0f, 0f, 1f, 0f
                )
            )

            PhotoFilter.DUOTONE_PURPLE_TEAL -> ColorMatrix(
                floatArrayOf(
                    0.5f, 0.2f, 0.8f, 0f, 0f,
                    0.1f, 0.5f, 0.6f, 0f, 0f,
                    0.8f, 0.3f, 0.5f, 0f, 0f,
                    0f, 0f, 0f, 1f, 0f
                )
            )

            PhotoFilter.DARK_MOODY -> ColorMatrix(
                floatArrayOf(
                    1.1f, -0.1f, 0f, 0f, -15f,
                    -0.05f, 1.0f, -0.1f, 0f, -10f,
                    0f, -0.1f, 1.2f, 0f, -5f,
                    0f, 0f, 0f, 1f, 0f
                )
            )

            PhotoFilter.DRAMATIC -> ColorMatrix(
                floatArrayOf(
                    1.4f, -0.1f, -0.1f, 0f, -20f,
                    -0.1f, 1.4f, -0.1f, 0f, -20f,
                    -0.1f, -0.1f, 1.4f, 0f, -20f,
                    0f, 0f, 0f, 1f, 0f
                )
            )

            PhotoFilter.SOFT_DREAM -> ColorMatrix(
                floatArrayOf(
                    1.1f, -0.1f, -0.1f, 0f, 20f,
                    -0.1f, 1.1f, -0.1f, 0f, 20f,
                    -0.1f, -0.1f, 1.1f, 0f, 20f,
                    0f, 0f, 0f, 1f, 0f
                )
            )

            PhotoFilter.HAZE -> ColorMatrix(
                floatArrayOf(
                    0.9f, 0.05f, 0.05f, 0f, 30f,
                    0.05f, 0.9f, 0.05f, 0f, 30f,
                    0.05f, 0.05f, 0.9f, 0f, 30f,
                    0f, 0f, 0f, 1f, 0f
                )
            )

            PhotoFilter.VIGNETTE -> ColorMatrix(
                floatArrayOf(
                    1.05f, -0.05f, -0.05f, 0f, -10f,
                    -0.05f, 1.05f, -0.05f, 0f, -10f,
                    -0.05f, -0.05f, 1.05f, 0f, -10f,
                    0f, 0f, 0f, 1f, 0f
                )
            )

            PhotoFilter.BLOSSOM -> ColorMatrix(
                floatArrayOf(
                    1.1f, 0.1f, 0.2f, 0f, 15f,
                    0.05f, 1.05f, 0.05f, 0f, 10f,
                    0.1f, 0.05f, 0.9f, 0f, 5f,
                    0f, 0f, 0f, 1f, 0f
                )
            )

            PhotoFilter.NIGHT_BLUE -> ColorMatrix(
                floatArrayOf(
                    0.8f, 0f,   0f,   0f, -10f,
                    0f,   0.9f, 0f,   0f, -5f,
                    0.2f, 0.2f, 1.3f, 0f, 15f,
                    0f, 0f, 0f, 1f, 0f
                )
            )

            PhotoFilter.CYBERPUNK -> ColorMatrix(
                floatArrayOf(
                    1.3f, -0.2f, 0.6f, 0f, 10f,
                    0f,   0.9f,  0.3f, 0f, 0f,
                    0.5f, 0.1f,  1.4f, 0f, 20f,
                    0f, 0f, 0f, 1f, 0f
                )
            )

            PhotoFilter.NEON_POP -> ColorMatrix(
                floatArrayOf(
                    1.4f, 0.1f, 0.1f, 0f, 10f,
                    0.1f, 1.4f, 0.1f, 0f, 10f,
                    0.1f, 0.1f, 1.4f, 0f, 10f,
                    0f, 0f, 0f, 1f, 0f
                )
            )
            PhotoFilter.GOLDEN_HOUR -> ColorMatrix(
                floatArrayOf(
                    1.2f, 0.1f, 0f,   0f, 20f,
                    0.05f, 1.1f, 0f,  0f, 10f,
                    0f,   0.05f, 0.9f,0f, -5f,
                    0f,   0f,    0f,  1f, 0f
                )
            )
            PhotoFilter.CINNAMON -> ColorMatrix(
                floatArrayOf(
                    1.15f, 0.05f, 0f, 0f, 10f,
                    0.05f, 1.05f, 0f, 0f, 5f,
                    0f,    0.05f, 0.85f,0f, -10f,
                    0f,    0f,    0f,   1f, 0f
                )
            )
            PhotoFilter.ROSE_GOLD -> ColorMatrix(
                floatArrayOf(
                    1.1f, 0.05f, 0.1f, 0f, 15f,
                    0.05f,1f,    0.15f,0f, 10f,
                    0.1f, 0.05f, 0.9f, 0f, -5f,
                    0f,   0f,    0f,   1f, 0f
                )
            )
            PhotoFilter.CARAMEL -> ColorMatrix(
                floatArrayOf(
                    1.1f, 0.1f, 0f,   0f, 15f,
                    0.05f,1.05f,0f,   0f, 10f,
                    0f,   0.05f,0.8f, 0f, -10f,
                    0f,   0f,   0f,   1f, 0f
                )
            )
            PhotoFilter.AMBER_GLOW -> ColorMatrix(
                floatArrayOf(
                    1.25f,0.1f,  0f,   0f, 25f,
                    0.1f, 1.1f,  0f,   0f, 15f,
                    0f,   0.05f, 0.85f,0f, -5f,
                    0f,   0f,    0f,   1f, 0f
                )
            )
            PhotoFilter.FROST -> ColorMatrix(
                floatArrayOf(
                    0.9f, 0.05f, 0.1f,  0f, 5f,
                    0.05f,0.95f, 0.15f, 0f, 5f,
                    0.1f, 0.1f,  1.2f,  0f, 10f,
                    0f,   0f,    0f,    1f, 0f
                )
            )
            PhotoFilter.GLACIER -> ColorMatrix(
                floatArrayOf(
                    0.85f,0.05f,0.2f, 0f, -10f,
                    0.05f,0.9f, 0.2f, 0f, -10f,
                    0.2f, 0.3f, 1.3f, 0f, 20f,
                    0f,   0f,   0f,   1f, 0f
                )
            )
            PhotoFilter.ARCTIC -> ColorMatrix(
                floatArrayOf(
                    0.8f, 0.1f, 0.1f,  0f, 0f,
                    0.1f, 0.85f,0.2f, 0f, 0f,
                    0.1f, 0.2f, 1.2f,  0f, 10f,
                    0f,   0f,   0f,    1f, 0f
                )
            )
            PhotoFilter.AURORA -> ColorMatrix(
                floatArrayOf(
                    0.9f, 0.15f,0f, 0f, 0f,
                    0.1f, 1f,   0.2f,0f, 10f,
                    0.2f, 0.3f, 1.1f,0f, 15f,
                    0f,   0f,   0f,  1f, 0f
                )
            )
            PhotoFilter.OCEAN_DEEP -> ColorMatrix(
                floatArrayOf(
                    0.7f, 0.05f,0.2f, 0f, -20f,
                    0.05f,0.75f,0.25f,0f, -10f,
                    0.2f, 0.3f, 1.3f, 0f, 20f,
                    0f,   0f,   0f,   1f, 0f
                )
            )
            PhotoFilter.AQUA_MINT -> ColorMatrix(
                floatArrayOf(
                    0.9f, 0.1f,  0.1f, 0f, 5f,
                    0.1f, 1.1f,  0.2f, 0f, 10f,
                    0.1f, 0.2f,  1.05f,0f, 5f,
                    0f,   0f,    0f,   1f, 0f
                )
            )
            PhotoFilter.EMERALD -> ColorMatrix(
                floatArrayOf(
                    0.9f, 0.2f, 0.1f, 0f, 0f,
                    0.15f,1.2f, 0.15f,0f, 20f,
                    0.05f,0.15f,0.9f,0f, -10f,
                    0f,    0f,   0f,   1f, 0f
                )
            )
            PhotoFilter.FOREST_FADE -> ColorMatrix(
                floatArrayOf(
                    0.85f,0.1f, 0.05f,0f, -10f,
                    0.1f, 1.05f,0.1f, 0f, 15f,
                    0.05f,0.15f,0.9f, 0f, -5f,
                    0f,   0f,   0f,   1f, 0f
                )
            )
            PhotoFilter.DESERT_SAND -> ColorMatrix(
                floatArrayOf(
                    1.1f, 0.15f, 0.05f, 0f, 10f,
                    0.1f, 1.05f, 0.05f, 0f, 5f,
                    0.05f,0.05f, 0.9f,  0f, -5f,
                    0f,   0f,    0f,    1f, 0f
                )
            )
            PhotoFilter.SUNSET -> ColorMatrix(
                floatArrayOf(
                    1.3f, 0.1f, 0.1f, 0f, 20f,
                    0.1f, 1.1f, 0.1f, 0f, 10f,
                    0.05f,0.1f, 0.9f, 0f, -10f,
                    0f,   0f,   0f,   1f, 0f
                )
            )
            PhotoFilter.DREAMY_PINK -> ColorMatrix(
                floatArrayOf(
                    1.1f, 0.1f, 0.2f, 0f, 20f,
                    0.1f, 1f,   0.2f, 0f, 10f,
                    0.1f, 0.1f, 0.9f, 0f, -5f,
                    0f,   0f,   0f,   1f, 0f
                )
            )
            PhotoFilter.BLOOM -> ColorMatrix(
                floatArrayOf(
                    1.2f, 0.1f,  0.1f, 0f, 20f,
                    0.1f, 1.1f,  0.1f, 0f, 20f,
                    0.1f, 0.1f,  1.05f,0f, 10f,
                    0f,   0f,    0f,   1f, 0f
                )
            )
            PhotoFilter.VELVET -> ColorMatrix(
                floatArrayOf(
                    1.05f,0.1f, 0.2f, 0f, 0f,
                    0.1f, 0.9f, 0.15f,0f, -5f,
                    0.1f, 0.15f,0.85f,0f, -10f,
                    0f,   0f,   0f,    1f, 0f
                )
            )
            PhotoFilter.NOIR_FADE -> ColorMatrix(
                floatArrayOf(
                    0.55f,0.55f,0.55f,0f, 10f,
                    0.45f,0.45f,0.45f,0f, 5f,
                    0.35f,0.35f,0.35f,0f, -5f,
                    0f,   0f,   0f,   1f, 0f
                )
            )
            else -> ColorMatrix()
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
        filter: PhotoFilter,
        emojiLayers : List<EmojiLayer>
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

        val filterMatrix = getFilterMatrix(filter)
        colorMatrix.postConcat(filterMatrix)

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

        emojiLayers.forEach { layer ->
            paint.textSize = 40.sp.value * layer.scale
            canvas.save()
            canvas.rotate(layer.rotation,layer.offset.x, layer.offset.y)
            canvas.drawText(layer.emoji, layer.offset.x, layer.offset.y, paint)
            canvas.restore()
        }

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