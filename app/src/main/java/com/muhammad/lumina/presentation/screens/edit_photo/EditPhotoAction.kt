package com.muhammad.lumina.presentation.screens.edit_photo

import com.muhammad.lumina.domain.model.EditPhotoFeature
import com.muhammad.lumina.domain.model.PhotoFilter

sealed interface EditPhotoAction{
    data class OnLoadEditPhoto(val photo : String) : EditPhotoAction
    data class OnSetBrightness(val value : Float) : EditPhotoAction
    data class OnSetContrast(val value : Float) : EditPhotoAction
    data class OnSetSaturation(val value : Float) : EditPhotoAction
    data class OnRotate(val degrees : Float) : EditPhotoAction
    data object OnToggleFlipHorizontal : EditPhotoAction
    data object OnToggleFlipVertical : EditPhotoAction
    data object OnResetAllEdits : EditPhotoAction
    data class OnSelectEditFeature(val feature: EditPhotoFeature) : EditPhotoAction
    data object OnToggleSavePhotoToGalleryDialog : EditPhotoAction
    data object OnSaveImageToGallery : EditPhotoAction
    data object OnToggleExitEditingDialog : EditPhotoAction
    data object OnConfirmExitEditing: EditPhotoAction
    data class OnSetPhotoFilter(val filter: PhotoFilter) : EditPhotoAction
}