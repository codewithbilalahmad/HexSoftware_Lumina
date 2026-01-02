package com.muhammad.lumina.presentation.screens.view_photo

sealed interface ViewPhotoAction{
    data class OnLoadPhoto(val photo : String) : ViewPhotoAction
}