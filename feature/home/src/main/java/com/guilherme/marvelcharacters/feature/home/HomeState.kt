package com.guilherme.marvelcharacters.feature.home

import androidx.annotation.StringRes
import com.guilherme.marvelcharacters.core.model.Character

internal data class HomeState(
    val characters: List<Character> = emptyList(),
    @StringRes val errorMessageId: Int? = null,
    val isLoading: Boolean = false
)
