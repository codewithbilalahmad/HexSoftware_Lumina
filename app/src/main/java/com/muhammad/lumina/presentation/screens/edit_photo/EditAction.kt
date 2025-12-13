package com.muhammad.lumina.presentation.screens.edit_photo

import com.muhammad.lumina.domain.model.PhotoFilter

sealed interface EditAction {
    data class Brightness(val previous: Float, val current: Float) : EditAction
    data class Contrast(val previous: Float, val current: Float) : EditAction
    data class Saturation(val previous: Float, val current: Float) : EditAction

    data class Rotation(val previous: Float, val degrees: Float) : EditAction
    data class FlipHorizontal(val flipped: Boolean) : EditAction
    data class FlipVertical(val flipped: Boolean) : EditAction

    data class FilterApplied(val previous: PhotoFilter, val current: PhotoFilter) : EditAction
    data class ResetAllEdits(
        val previousBrightness: Float,
        val previousContrast: Float,
        val previousSaturation: Float,
        val previousRotation: Float,
        val previousFlipHorizontal: Boolean,
        val previousFlipVertical: Boolean,
        val previousFilter: PhotoFilter
    ) : EditAction
}