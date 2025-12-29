package com.muhammad.lumina.domain.model

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import com.muhammad.lumina.R

@Immutable
enum class EditPhotoFeature(val icon : Int, @get:StringRes val  label : Int) {
    Filters(icon = R.drawable.ic_filters, label = R.string.filters),
    Emoji(icon = R.drawable.ic_emoji, label = R.string.emoji),
    Brightness(icon = R.drawable.ic_brightness, label = R.string.brightness),
    Contrast(icon = R.drawable.ic_contrast, label = R.string.contrast),
    Saturation(icon = R.drawable.ic_saturation, label = R.string.saturation),
    RotateLeft(icon = R.drawable.ic_rotate_left, label = R.string.rotate),
    RotateRight(icon = R.drawable.ic_rotate_right, label = R.string.rotate),
    FlipHorizontal(icon = R.drawable.ic_flip_horizontal, label = R.string.horizontal),
    FlipVertical(icon = R.drawable.ic_flip_vertical, label = R.string.vertical),
    Reset(icon = R.drawable.ic_reset, label = R.string.reset)
}