package com.guilherme.marvelcharacters.feature.favorites

import com.guilherme.marvelcharacters.core.model.Character

internal data class FavoritesState(
    val characters: List<Character> = emptyList()
)