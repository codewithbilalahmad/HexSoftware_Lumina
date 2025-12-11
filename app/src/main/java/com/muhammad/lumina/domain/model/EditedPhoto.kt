package com.muhammad.lumina.domain.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.muhammad.lumina.utils.createBeautifulGradient

@Immutable
data class EditedPhoto(
    val id : Long, val uri : String, val colors : List<Color> = createBeautifulGradient()
)
