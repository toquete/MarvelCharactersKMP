package com.guilherme.marvelcharacters.feature.favorites

import androidx.annotation.StringRes
import com.guilherme.marvelcharacters.core.model.Character

internal data class FavoritesState(
    val characters: List<Character> = emptyList(),
    @StringRes val messageId: Int? = null
)