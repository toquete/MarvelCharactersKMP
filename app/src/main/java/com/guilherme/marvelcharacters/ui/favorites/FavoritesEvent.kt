package com.guilherme.marvelcharacters.ui.favorites

import androidx.annotation.StringRes
import com.guilherme.marvelcharacters.infrastructure.Event
import com.guilherme.marvelcharacters.model.CharacterVO

sealed class FavoritesEvent : Event {
    data class ShowSnackbarMessage(@StringRes val messageId: Int) : FavoritesEvent()
    data class NavigateToDetail(val character: CharacterVO) : FavoritesEvent()
}
