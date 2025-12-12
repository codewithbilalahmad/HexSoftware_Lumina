package com.muhammad.lumina.presentation.screens.edit_photo

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.muhammad.lumina.data.EditHistoryManager
import com.muhammad.lumina.domain.model.EditPhotoFeature
import com.muhammad.lumina.domain.model.PhotoFilter
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
    private val editHistoryManager: EditHistoryManager,
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
            is EditPhotoAction.OnSetPhotoFilter -> onSetPhotoFilter(action.filter)
            EditPhotoAction.OnRedoEdit -> onRedoEdit()
            EditPhotoAction.OnUndoEdit -> onUndoEdit()
        }
    }

    private fun onUndoEdit() {
        val action = editHistoryManager.undo() ?: return
        when (action) {
            is EditAction.Brightness -> {
                _state.update { it.copy(brightness = action.previous) }
            }

            is EditAction.Contrast -> {
                _state.update { it.copy(contrast = action.previous) }
            }

            is EditAction.FilterApplied -> {
                _state.update { it.copy(selectedPhotoFilter = action.previous) }
            }

            is EditAction.FlipHorizontal -> {
                _state.update { it.copy(flipHorizontal = action.flipped) }
            }

            is EditAction.FlipVertical -> {
                _state.update { it.copy(flipVertical = action.flipped) }
            }

            is EditAction.Rotation -> {
                _state.update { it.copy(rotation = action.previous % 360f) }
            }

            is EditAction.Saturation -> {
                _state.update { it.copy(saturation = action.previous) }
            }
        }
        applyAllEdits()
    }

    private fun onRedoEdit() {
        val action = editHistoryManager.redo() ?: return
        when (action) {
            is EditAction.Brightness -> {
                _state.update { it.copy(brightness = action.current) }
            }

            is EditAction.Contrast -> {
                _state.update { it.copy(contrast = action.current) }
            }

            is EditAction.FilterApplied -> {
                _state.update { it.copy(selectedPhotoFilter = action.previous) }
            }

            is EditAction.FlipHorizontal -> {
                _state.update { it.copy(flipHorizontal = !action.flipped) }
            }

            is EditAction.FlipVertical -> {
                _state.update { it.copy(flipVertical = !action.flipped) }
            }

            is EditAction.Rotation -> {
                val newRotation = (action.previous + action.degrees) % 360f
                _state.update { it.copy(rotation = newRotation) }
            }

            is EditAction.Saturation -> {
                _state.update { it.copy(saturation = action.current) }
            }
        }
        applyAllEdits()
    }

    private fun onSetPhotoFilter(filter: PhotoFilter) {
        val prev = _state.value.selectedPhotoFilter
        editHistoryManager.push(
            EditAction.FilterApplied(
                previous = prev,
                current = filter
            )
        )
        _state.update { it.copy(selectedPhotoFilter = filter) }
        applyAllEdits()
    }

    private fun onToggleExitEditingDialog() {
        _state.update { it.copy(showExitEditingDialog = !it.showExitEditingDialog) }
    }

    private fun onConfirmExitEditing() {
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
        val prev = _state.value.rotation
        editHistoryManager.push(EditAction.Rotation(previous = prev, degrees = degrees))
        _state.update { it.copy(rotation = (prev + degrees) % 360f) }
        applyAllEdits()
    }

    private fun onSetBrightness(value: Float) {
        val prev = _state.value.brightness
        editHistoryManager.push(
            EditAction.Brightness(
                previous = prev,
                current = value
            )
        )
        _state.update { it.copy(brightness = value) }
        applyAllEdits()
    }

    private fun onSetContrast(value: Float) {
        val prev = _state.value.contrast
        editHistoryManager.push(
            EditAction.Contrast(
                previous = prev,
                current = value
            )
        )
        _state.update { it.copy(contrast = value) }
        applyAllEdits()
    }

    private fun onSetSaturation(value: Float) {
        val prev = _state.value.saturation
        editHistoryManager.push(
            EditAction.Contrast(
                previous = prev,
                current = value
            )
        )
        _state.update { it.copy(saturation = value) }
        applyAllEdits()
    }

    private fun onToggleFlipHorizontal() {
        val prev = _state.value.flipHorizontal
        editHistoryManager.push(EditAction.FlipHorizontal(prev))
        _state.update { it.copy(flipHorizontal = !prev) }
        applyAllEdits()
    }

    private fun onToggleFlipVertical() {
        val prev = _state.value.flipVertical
        editHistoryManager.push(EditAction.FlipVertical(prev))
        _state.update { it.copy(flipVertical = prev) }
        applyAllEdits()
    }

    private fun onResetAllEdits() {
        val current = _state.value
        editHistoryManager.push(EditAction.Brightness(previous = current.brightness, current = 0f))
        editHistoryManager.push(EditAction.Contrast(previous = current.contrast, current = 1f))
        editHistoryManager.push(EditAction.Saturation(previous = current.saturation, current = 1f))
        editHistoryManager.push(EditAction.Rotation(previous = current.rotation, degrees = 0f))
        editHistoryManager.push(EditAction.FlipHorizontal(flipped = current.flipHorizontal))
        editHistoryManager.push(EditAction.FlipVertical(flipped = current.flipVertical))
        editHistoryManager.push(
            EditAction.FilterApplied(
                previous = current.selectedPhotoFilter,
                current = PhotoFilter.NORMAL
            )
        )
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

    fun canUndo(): Boolean = editHistoryManager.canUndo()
    fun canRedo(): Boolean = editHistoryManager.canRedo()

    private fun onLoadEditPhoto(photo: String) {
        editHistoryManager.clear()
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
            flipHorizontal = state.flipHorizontal, filter = state.selectedPhotoFilter
        )

        _state.update { it.copy(editedBitmap = edited) }
    }
}
