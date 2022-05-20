package com.guilherme.marvelcharacters.ui.detail

import com.guilherme.marvelcharacters.domain.model.Character
import com.guilherme.marvelcharacters.infrastructure.State

data class DetailState(val isFavorite: Boolean = false) : State

data class DetailComposeState(
    val character: Character? = null
)