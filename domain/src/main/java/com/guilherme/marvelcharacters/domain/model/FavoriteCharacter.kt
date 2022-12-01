package com.guilherme.marvelcharacters.domain.model

import com.guilherme.marvelcharacters.core.model.Character

data class FavoriteCharacter(
    val character: Character,
    val isFavorite: Boolean
)
