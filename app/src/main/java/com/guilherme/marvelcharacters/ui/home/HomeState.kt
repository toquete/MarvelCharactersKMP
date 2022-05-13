package com.guilherme.marvelcharacters.ui.home

import androidx.annotation.StringRes
import com.guilherme.marvelcharacters.infrastructure.State
import com.guilherme.marvelcharacters.model.CharacterVO

data class HomeState(
    val isLoading: Boolean = false,
    val characters: List<CharacterVO> = emptyList(),
    @StringRes val errorMessageId: Int? = null
) : State
