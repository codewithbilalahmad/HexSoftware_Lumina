package com.muhammad.lumina.utils

import androidx.compose.material3.SnackbarDuration

sealed interface SnackbarEvent {
    data class ShowSnackbar(
        val message: String,
        val icon : Int,
        val actionLabel: String? = null,
        val actionClick : () -> Unit = {},
        val duration: SnackbarDuration = SnackbarDuration.Short
    ) : SnackbarEvent
}
