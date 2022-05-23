package com.guilherme.marvelcharacters.ui.detail

import androidx.annotation.StringRes
import com.guilherme.marvelcharacters.domain.model.Character
import com.guilherme.marvelcharacters.infrastructure.State

data class DetailState(val isFavorite: Boolean = false) : State

data class SnackbarMessage(
    @StringRes val message: Int,
    val showAction: Boolean = false
)

data class DetailComposeState(
    val character: Character? = null,
    val message: SnackbarMessage? = null
)