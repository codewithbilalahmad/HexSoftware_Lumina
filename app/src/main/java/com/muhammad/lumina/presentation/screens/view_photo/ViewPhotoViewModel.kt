package com.muhammad.lumina.presentation.screens.view_photo

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.muhammad.lumina.presentation.navigation.Destinations
import com.muhammad.lumina.utils.decodeBitmapFromPath
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ViewPhotoViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel(){
    private val photo = savedStateHandle.toRoute<Destinations.ViewPhotoScreen>().photo
    private val _state = MutableStateFlow(ViewPhotoState())
    val state=  _state.asStateFlow()
    init {
        onAction(ViewPhotoAction.OnLoadPhoto(photo))
    }
    fun onAction(action: ViewPhotoAction){
        when(action){
            is ViewPhotoAction.OnLoadPhoto -> onLoadPhoto(action.photo)
            ViewPhotoAction.OnToggleOptionsDropdown -> onToggleOptionsDropdown()
        }
    }

    private fun onToggleOptionsDropdown() {
        _state.update { it.copy(showOptionsDropdown = !it.showOptionsDropdown) }
    }

    private fun onLoadPhoto(photo: String) {
        val photoBitmap = decodeBitmapFromPath(photo) ?: return
        _state.update { it.copy(photoBitmap = photoBitmap, photoUrl = photo) }
    }
}