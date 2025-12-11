package com.muhammad.lumina.presentation.screens.home

import com.muhammad.lumina.domain.model.EditedPhoto

sealed interface HomeAction{
    data class OnPhotoSelected(val photo : String) : HomeAction
    data class OnEditedPhotoSelected(val editedPhoto: EditedPhoto?) : HomeAction
    data object GetEditedPhotos : HomeAction
    data object OnClearPhoto : HomeAction
}