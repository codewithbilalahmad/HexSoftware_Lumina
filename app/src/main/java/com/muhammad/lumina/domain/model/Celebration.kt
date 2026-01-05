package com.muhammad.lumina.domain.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
data class Celebration(
    var x : Float,
    var y : Float,
    var vx : Float,
    var vy : Float,
    var size : Float,
    var color : Color,
    var alpha : Float,
    var rotation : Float,
    var rotationSpeed : Float,
    var type : CelebrationType
)
