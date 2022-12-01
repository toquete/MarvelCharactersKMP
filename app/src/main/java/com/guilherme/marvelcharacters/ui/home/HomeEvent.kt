package com.guilherme.marvelcharacters.ui.home

import com.guilherme.marvelcharacters.core.model.Character
import com.guilherme.marvelcharacters.infrastructure.Event

sealed class HomeEvent : Event {
    data class NavigateToDetails(val character: Character) : HomeEvent()
}
