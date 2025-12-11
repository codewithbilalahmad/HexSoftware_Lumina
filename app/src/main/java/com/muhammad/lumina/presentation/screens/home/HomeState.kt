package com.muhammad.lumina.presentation.screens.home

import android.graphics.Bitmap
import com.muhammad.lumina.R
import com.muhammad.lumina.domain.model.AppFeature
import com.muhammad.lumina.domain.model.EditedPhoto

data class HomeState(
    val selectedPhoto: String? = null,
    val selectedEditedPhoto: EditedPhoto?=null,
    val editedPhotos : List<EditedPhoto> = emptyList(),
    val selectedPhotoBitmap: Bitmap? = null,
    val appFeatures: List<AppFeature> = listOf(
        AppFeature(
            icon = R.drawable.ic_brightness,
            title = R.string.adjust_brightness,
            description = R.string.adjust_brightness_desp
        ),
        AppFeature(
            icon = R.drawable.ic_contrast,
            title = R.string.adjust_contrast,
            description = R.string.adjust_contrast_desp
        ),
        AppFeature(
            icon = R.drawable.ic_saturation,
            title = R.string.adjust_saturation,
            description = R.string.adjust_saturation_desp
        ),
        AppFeature(
            icon = R.drawable.ic_rotate,
            title = R.string.rotate_photo,
            description = R.string.rotate_photo_desp
        ),
        AppFeature(
            icon = R.drawable.ic_flip_image,
            title = R.string.flip_photo,
            description = R.string.flip_photo_desp
        ),
    ),
)