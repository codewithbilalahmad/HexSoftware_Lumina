package com.muhammad.lumina.domain.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.geometry.Offset
import java.util.UUID

@Immutable
data class EmojiLayer(
    val id : String = UUID.randomUUID().toString(),
    val emoji : String,
    val offset : Offset = Offset.Zero,
    val scale : Float = 1f,
    val rotation : Float = 0f
)
