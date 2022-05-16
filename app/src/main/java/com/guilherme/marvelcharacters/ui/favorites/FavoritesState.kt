package com.guilherme.marvelcharacters.ui.favorites

import androidx.annotation.StringRes
import com.guilherme.marvelcharacters.infrastructure.State
import com.guilherme.marvelcharacters.model.CharacterVO

data class FavoritesState(
    val list: List<CharacterVO> = emptyList(),
    @StringRes val messageId: Int? = null
) : State
