package com.muhammad.lumina.presentation.screens.edit_photo

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.muhammad.lumina.domain.model.EditPhotoFeature
import com.muhammad.lumina.domain.repository.ImageUtilsRepository
import com.muhammad.lumina.presentation.navigation.Destinations
import com.muhammad.lumina.utils.SnackbarEvent
import com.muhammad.lumina.utils.decodeBitmapFromPath
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.Float

class EditPhotoViewModel(
    savedStateHandle: SavedStateHandle,
    private val imageUtilsRepository: ImageUtilsRepository,
) : ViewModel() {

    private val photo = savedStateHandle.toRoute<Destinations.EditPhotoScreen>().photo

    private val _state = MutableStateFlow(EditPhotoState())
    val state = _state.asStateFlow()
    private val _snackbarEvents = Channel<SnackbarEvent>()
    val snackbarEvents = _snackbarEvents.receiveAsFlow()
    private val _events = Channel<EditPhotoEvents>()
    val events = _events.receiveAsFlow()
    init {
        onAction(EditPhotoAction.OnLoadEditPhoto(photo))
    }
    fun onAction(action: EditPhotoAction) {
        when (action) {
            is EditPhotoAction.OnLoadEditPhoto -> onLoadEditPhoto(action.photo)
            is EditPhotoAction.OnRotate -> onRotate(action.degrees)
            is EditPhotoAction.OnSetBrightness -> onSetBrightness(action.value)
            is EditPhotoAction.OnSetContrast -> onSetContrast(action.value)
            is EditPhotoAction.OnSetSaturation -> onSetSaturation(action.value)
            EditPhotoAction.OnToggleFlipHorizontal -> onToggleFlipHorizontal()
            EditPhotoAction.OnToggleFlipVertical -> onToggleFlipVertical()
            EditPhotoAction.OnResetAllEdits -> onResetAllEdits()
            is EditPhotoAction.OnSelectEditFeature -> onSelectEditFeature(action.feature)
            EditPhotoAction.OnToggleSavePhotoToGalleryDialog -> onToggleSavePhotoToGalleryDialog()
            EditPhotoAction.OnSaveImageToGallery -> onSaveImageToGallery()
            EditPhotoAction.OnConfirmExitEditing -> onConfirmExitEditing()
            EditPhotoAction.OnToggleExitEditingDialog -> onToggleExitEditingDialog()
        }
    }

    private fun onToggleExitEditingDialog() {
        _state.update { it.copy(showExitEditingDialog = !it.showExitEditingDialog) }
    }

    private fun onConfirmExitEditing(){
        viewModelScope.launch {
            _state.update { it.copy(showExitEditingDialog = false) }
            _events.send(EditPhotoEvents.OnNavigateUp)
        }
    }
    private fun onSaveImageToGallery() {
        viewModelScope.launch {
            val editedBitmap = _state.value.editedBitmap ?: return@launch
            _state.update { it.copy(isSavingImageToGallery = true) }
            val uri = imageUtilsRepository.saveImageInExternalStorage(bitmap = editedBitmap)
            if (uri != null) {
                _state.update {
                    it.copy(
                        isSavingImageToGallery = false,
                        showSaveImageToGalleryDialog = false
                    )
                }
                _snackbarEvents.send(SnackbarEvent.ShowSnackbar(message = "Image Saved in Gallery"))
            } else {
                _state.update {
                    it.copy(
                        isSavingImageToGallery = false,
                        showSaveImageToGalleryDialog = false
                    )
                }
                _snackbarEvents.send(SnackbarEvent.ShowSnackbar(message = "Failed to Save Image"))
            }
        }
    }

    private fun onToggleSavePhotoToGalleryDialog() {
        _state.update { it.copy(showSaveImageToGalleryDialog = !it.showSaveImageToGalleryDialog) }
    }

    private fun onSelectEditFeature(feature: EditPhotoFeature) {
        _state.update { it.copy(selectedFeature = feature) }
    }

    private fun onRotate(degrees: Float) {
        _state.update { it.copy(rotation = (state.value.rotation + degrees) % 360f) }
        applyAllEdits()
    }

    private fun onSetBrightness(value: Float) {
        _state.update { it.copy(brightness = value) }
        applyAllEdits()
    }

    private fun onSetContrast(value: Float) {
        _state.update { it.copy(contrast = value) }
        applyAllEdits()
    }

    private fun onSetSaturation(value: Float) {
        _state.update { it.copy(saturation = value) }
        applyAllEdits()
    }

    private fun onToggleFlipHorizontal() {
        _state.update { it.copy(flipHorizontal = !state.value.flipHorizontal) }
        applyAllEdits()
    }

    private fun onToggleFlipVertical() {
        _state.update { it.copy(flipVertical = !state.value.flipVertical) }
        applyAllEdits()
    }

    private fun onResetAllEdits() {
        _state.update {
            it.copy(
                brightness = 0f,
                contrast = 1f,
                saturation = 1f,
                rotation = 0f,
                flipHorizontal = false,
                flipVertical = false
            )
        }
        applyAllEdits()
    }

    private fun onLoadEditPhoto(photo: String) {
        val original = decodeBitmapFromPath(photo) ?: return
        _state.update {
            it.copy(
                originalBitmap = original,
                editedBitmap = original
            )
        }

        applyAllEdits()
    }

    private fun applyAllEdits() {
        val state = _state.value
        val baseBitmap = state.originalBitmap ?: return

        val edited = imageUtilsRepository.applyEditsToBitmap(
            bitmap = baseBitmap,
            brightness = state.brightness,
            contrast = state.contrast,
            saturation = state.saturation,
            rotation = state.rotation,
            flipVertical = state.flipVertical,
            flipHorizontal = state.flipHorizontal
        )

        _state.update { it.copy(editedBitmap = edited) }
    }
}
