package com.guilherme.marvelcharacters.core.ui

import androidx.compose.material3.SnackbarDuration

data class SnackbarMessage(
    val text: String,
    val actionLabel: String? = null,
    val withDismissAction: Boolean = false,
    val duration: SnackbarDuration = SnackbarDuration.Short
)
