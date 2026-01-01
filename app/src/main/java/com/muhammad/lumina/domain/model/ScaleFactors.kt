package com.muhammad.lumina.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class ScaleFactors(
    val scaleX : Float,
    val scaleY : Float,
    val bitmapScale : Float
)
