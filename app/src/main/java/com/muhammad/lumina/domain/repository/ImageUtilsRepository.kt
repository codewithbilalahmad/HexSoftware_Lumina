package com.muhammad.lumina.domain.repository

import android.graphics.Bitmap
import android.net.Uri
import com.muhammad.lumina.domain.model.EditedPhoto
import com.muhammad.lumina.domain.model.PhotoFilter
import kotlinx.coroutines.flow.StateFlow
import java.io.OutputStream

interface ImageUtilsRepository {
    val editedPhotos : StateFlow<List<EditedPhoto>>
    fun loadEditedPhotos()
    fun applyEditsToBitmap(
        bitmap: Bitmap,
        brightness: Float,
        contrast: Float,
        saturation: Float,
        rotation: Float,
        flipVertical: Boolean,
        flipHorizontal: Boolean,
        filter : PhotoFilter
    ): Bitmap

    fun saveBitmapToStream(bitmap: Bitmap, out: OutputStream)
    fun saveImageInExternalStorage(filePath : String): Uri?
    fun shareEditedImage(filePath : String)
    fun getImagesFromLuminaFolder(): List<EditedPhoto>
}