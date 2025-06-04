package com.guilherme.marvelcharacters.feature.favorites

import com.guilherme.marvelcharacters.core.model.Character
import com.guilherme.marvelcharacters.core.ui.SnackbarMessageMP

internal data class FavoritesState(
    val characters: List<Character> = emptyList(),
    val snackbarMessage: SnackbarMessageMP? = null
)