package com.muhammad.lumina.domain.model

import androidx.compose.runtime.Immutable
import java.util.UUID

@Immutable
data class Emoji(
    val id : String = UUID.randomUUID().toString(),
    val emoji : String,
    val type : EmojiType
)
