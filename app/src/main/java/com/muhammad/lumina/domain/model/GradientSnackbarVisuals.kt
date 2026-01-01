package com.muhammad.lumina.domain.model

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.runtime.Immutable

@Immutable
data class GradientSnackbarVisuals(
    val icon : Int,
    override val actionLabel: String?,
    override val duration: SnackbarDuration,
    override val message: String,
    override val withDismissAction: Boolean
) : SnackbarVisuals