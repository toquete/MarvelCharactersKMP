package com.guilherme.marvelcharacters.core.ui

import androidx.compose.material3.SnackbarDuration

data class SnackbarMessageMP(
    val text: UiTextMP,
    val actionLabel: UiTextMP? = null,
    val withDismissAction: Boolean = false,
    val duration: SnackbarDuration = SnackbarDuration.Short
)
