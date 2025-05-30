package com.guilherme.marvelcharacters.feature.detail

import com.guilherme.marvelcharacters.core.ui.SnackbarMessageMP
import com.guilherme.marvelcharacters.domain.model.FavoriteCharacter

internal data class DetailState(
    val character: FavoriteCharacter? = null,
    var snackbarMessage: SnackbarMessageMP? = null
)
