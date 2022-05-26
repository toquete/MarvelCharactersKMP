package com.guilherme.marvelcharacters.ui.home

import androidx.annotation.StringRes
import com.guilherme.marvelcharacters.domain.model.Character

data class HomeState(
    val isLoading: Boolean = false,
    val characters: List<Character> = emptyList(),
    @StringRes val errorMessageId: Int? = null
)