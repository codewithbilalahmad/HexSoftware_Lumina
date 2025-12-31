package com.muhammad.lumina.presentation.screens.edit_photo

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntSize
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
    data object OnToggleEmojiPickerBottomSheet : EditPhotoAction
    data object OnSaveImageToGallery : EditPhotoAction
    data object OnToggleExitEditingDialog : EditPhotoAction
    data object OnConfirmExitEditing: EditPhotoAction
    data class OnSetPhotoFilter(val filter: PhotoFilter) : EditPhotoAction
    data object OnTapOutsideSelectedChild : EditPhotoAction
    data class OnAddEmojiClick(val emoji : String) : EditPhotoAction
    data object OnAddTextClick : EditPhotoAction
    data class OnEditChildText(val id : String) : EditPhotoAction
    data class OnSelectChild(val id : String) : EditPhotoAction
    data class OnEditTextChange(val id : String, val text : String) : EditPhotoAction
    data class OnDeleteChild(val id : String) : EditPhotoAction
    data class OnEditPhotoSizeChange(val size : IntSize) : EditPhotoAction
    data class OnChildTransformChange(
        val id : String,
        val offset : Offset,
        val scale : Float,
        val rotation : Float
    ) : EditPhotoAction
    data object OnToggleEditMenuDropdown : EditPhotoAction
    data object OnUndoEdit : EditPhotoAction
    data object OnRedoEdit : EditPhotoAction
}