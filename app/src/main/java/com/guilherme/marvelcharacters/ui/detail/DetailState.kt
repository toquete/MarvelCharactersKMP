package com.guilherme.marvelcharacters.ui.detail

import androidx.annotation.StringRes
import com.guilherme.marvelcharacters.domain.model.Character

data class SnackbarMessage(
    @StringRes val messageId: Int,
    val showAction: Boolean = false
)

data class DetailComposeState(
    val character: Character? = null,
    val isFavorite: Boolean = false,
    val message: SnackbarMessage? = null
)