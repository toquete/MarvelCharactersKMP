package com.guilherme.marvelcharacters.ui.home

import androidx.annotation.StringRes
import com.guilherme.marvelcharacters.core.model.Character

sealed interface HomeUiState {
    data class Success(val characters: List<Character>) : HomeUiState
    data class Error(@StringRes val errorMessageId: Int) : HomeUiState
    object Loading : HomeUiState
    object Empty : HomeUiState
}
