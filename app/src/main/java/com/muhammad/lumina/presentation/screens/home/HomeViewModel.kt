package com.muhammad.lumina.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muhammad.lumina.domain.model.EditedPhoto
import com.muhammad.lumina.domain.repository.ImageUtilsRepository
import com.muhammad.lumina.utils.decodeBitmapFromPath
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val imageUtilsRepository: ImageUtilsRepository
) : ViewModel() {
    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()
    init {
        onAction(HomeAction.GetEditedPhotos)
    }
    fun onAction(action: HomeAction) {
        when (action) {
            is HomeAction.OnPhotoSelected -> onPhotoSelected(action.photo)
            HomeAction.OnClearPhoto -> onClearPhoto()
            HomeAction.GetEditedPhotos -> getEditedPhotos()
            is HomeAction.OnEditedPhotoSelected -> onEditedPhotoSelected(action.editedPhoto)
        }
    }
    private fun onEditedPhotoSelected(editedPhoto: EditedPhoto?) {
        _state.update { it.copy(selectedEditedPhoto = editedPhoto) }
    }

    private fun getEditedPhotos() {
        viewModelScope.launch {
            imageUtilsRepository.editedPhotos.collectLatest { photos ->
                _state.update { it.copy(editedPhotos = photos) }
            }
        }
    }

    private fun onClearPhoto() {
        _state.update { it.copy(selectedPhoto = null, selectedPhotoBitmap = null) }
    }

    private fun onPhotoSelected(photo: String) {
        _state.update {
            it.copy(
                selectedPhoto = photo,
                selectedPhotoBitmap = decodeBitmapFromPath(photo)
            )
        }
    }
}