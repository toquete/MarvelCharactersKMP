package com.guilherme.marvelcharacters.feature.detail

import com.guilherme.marvelcharacters.core.ui.SnackbarMessage
import com.guilherme.marvelcharacters.domain.model.FavoriteCharacter

internal data class DetailState(
    val character: FavoriteCharacter? = null,
    var snackbarMessage: SnackbarMessage? = null
)
