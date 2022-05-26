package com.guilherme.marvelcharacters.ui.favorites

import androidx.annotation.StringRes
import com.guilherme.marvelcharacters.domain.model.Character

data class FavoritesState(
    val list: List<Character> = emptyList(),
    @StringRes val messageId: Int? = null
)