package com.guilherme.marvelcharacters.feature.detail

import androidx.annotation.StringRes
import com.guilherme.marvelcharacters.domain.model.FavoriteCharacter

internal sealed interface DetailUiState {
    data class Success(val character: FavoriteCharacter) : DetailUiState
    data class ShowSnackbar(@StringRes val messageId: Int?, val showAction: Boolean) : DetailUiState
    object Loading : DetailUiState
}