package com.muhammad.lumina.domain.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Immutable
data class Child(
    val id : String,
    val text : String,
    val isEmoji : Boolean,
    val fontSize : TextUnit = if(isEmoji) 40.sp else 36.sp,
    val offsetRatioX : Float = 0f,
    val offsetRatioY : Float = 0f,
    val scale : Float = 1f,
    val rotation : Float = 0f
)
