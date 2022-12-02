package com.guilherme.marvelcharacters.ui.detail

import androidx.annotation.StringRes
import com.guilherme.marvelcharacters.domain.model.FavoriteCharacter

sealed interface DetailUiState {
    data class Success(val character: FavoriteCharacter) : DetailUiState
    data class ShowSnackbar(@StringRes val messageId: Int?, val showAction: Boolean) : DetailUiState
    object Loading : DetailUiState
}