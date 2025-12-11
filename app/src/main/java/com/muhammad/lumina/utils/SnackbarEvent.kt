package com.muhammad.lumina.utils

import androidx.compose.material3.SnackbarDuration

sealed interface SnackbarEvent {
    data class ShowSnackbar(
        val message: String,
        val duration: SnackbarDuration = SnackbarDuration.Short
    ) : SnackbarEvent
}
