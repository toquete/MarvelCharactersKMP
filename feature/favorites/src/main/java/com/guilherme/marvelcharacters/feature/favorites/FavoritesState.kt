package com.guilherme.marvelcharacters.feature.favorites

import androidx.annotation.StringRes
import com.guilherme.marvelcharacters.core.model.Character
import com.guilherme.marvelcharacters.core.ui.SnackbarMessage

internal data class FavoritesState(
    val characters: List<Character> = emptyList(),
    @StringRes val messageId: Int? = null,
    val snackbarMessage: SnackbarMessage? = null
)