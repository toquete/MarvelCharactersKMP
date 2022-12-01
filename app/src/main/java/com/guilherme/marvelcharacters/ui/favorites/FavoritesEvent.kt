package com.guilherme.marvelcharacters.ui.favorites

import androidx.annotation.StringRes
import com.guilherme.marvelcharacters.core.model.Character
import com.guilherme.marvelcharacters.infrastructure.Event

sealed class FavoritesEvent : Event {
    data class ShowSnackbarMessage(@StringRes val messageId: Int) : FavoritesEvent()
    data class NavigateToDetail(val character: Character) : FavoritesEvent()
}
