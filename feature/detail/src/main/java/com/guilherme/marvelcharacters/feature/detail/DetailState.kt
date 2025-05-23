package com.guilherme.marvelcharacters.feature.detail

import androidx.annotation.StringRes
import com.guilherme.marvelcharacters.domain.model.FavoriteCharacter

internal data class DetailState(
    val character: FavoriteCharacter? = null,
    @StringRes val messageId: Int? = null,
    val showAction: Boolean = false
)
