package com.muhammad.lumina.domain.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.geometry.Offset

@Immutable
data class ScaledChild(
    val text : String,
    val scaledOffset : Offset,
    val strokeWidth : Float,
    val scaledFontSizePx : Float,
    val constraintWidth : Int,
    val textPaddingX : Float,
    val textPaddingY : Float,
    val rotation : Float,
    val scale : Float,
    val originalChild : Child
)
