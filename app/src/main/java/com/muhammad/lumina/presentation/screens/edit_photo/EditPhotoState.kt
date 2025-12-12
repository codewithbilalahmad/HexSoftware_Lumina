package com.muhammad.lumina.presentation.screens.edit_photo

import android.graphics.Bitmap
import com.muhammad.lumina.domain.model.EditPhotoFeature
import com.muhammad.lumina.domain.model.PhotoFilter

data class EditPhotoState(
    val originalBitmap: Bitmap? = null,
    val editedBitmap: Bitmap? = null,
    val selectedPhotoFilter : PhotoFilter = PhotoFilter.NORMAL,
    val showSaveImageToGalleryDialog : Boolean = false,
    val showExitEditingDialog : Boolean = false,
    val selectedFeature: EditPhotoFeature?=null,
    val isSavingImageToGallery : Boolean = false,
    val brightness : Float = 0f,
    val contrast : Float = 1f,
    val saturation : Float = 1f,
    val rotation : Float = 0f,
    val flipHorizontal : Boolean = false,
    val flipVertical : Boolean = false
)
