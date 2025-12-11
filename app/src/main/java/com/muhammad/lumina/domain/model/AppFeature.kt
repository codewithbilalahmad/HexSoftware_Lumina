package com.muhammad.lumina.domain.model

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.muhammad.lumina.utils.createBeautifulGradient

@Immutable
data class AppFeature(
    val icon : Int,
    @get:StringRes val title : Int,
    @get:StringRes val description : Int,
    val colors : List<Color> = createBeautifulGradient()
)