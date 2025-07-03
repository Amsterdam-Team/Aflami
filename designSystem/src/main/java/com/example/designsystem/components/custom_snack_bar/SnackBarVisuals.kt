package com.example.designsystem.components.custom_snack_bar

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarVisuals

data class SnackBarVisuals(
    override val message: String,
    val status: SnackBarStatus,
) : SnackbarVisuals {
    override val actionLabel: String? = null
    override val withDismissAction: Boolean = false
    override val duration: SnackbarDuration = SnackbarDuration.Short
}