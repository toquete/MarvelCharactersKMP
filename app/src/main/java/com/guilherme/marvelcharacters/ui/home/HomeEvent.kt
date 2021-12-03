package com.guilherme.marvelcharacters.ui.home

import com.guilherme.marvelcharacters.infrastructure.Event
import com.guilherme.marvelcharacters.model.CharacterVO

sealed class HomeEvent : Event {
    data class NavigateToDetails(val character: CharacterVO) : HomeEvent()
}
