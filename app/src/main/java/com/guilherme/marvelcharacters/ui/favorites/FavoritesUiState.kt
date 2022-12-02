package com.guilherme.marvelcharacters.ui.favorites

import androidx.annotation.StringRes
import com.guilherme.marvelcharacters.core.model.Character

sealed interface FavoritesUiState {
    data class Success(val list: List<Character> = emptyList()): FavoritesUiState
    data class ShowSnackbar(@StringRes val messageId: Int?): FavoritesUiState
}
