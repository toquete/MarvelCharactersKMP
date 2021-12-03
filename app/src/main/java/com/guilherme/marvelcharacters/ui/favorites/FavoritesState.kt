package com.guilherme.marvelcharacters.ui.favorites

import com.guilherme.marvelcharacters.infrastructure.State
import com.guilherme.marvelcharacters.model.CharacterVO

data class FavoritesState(val list: List<CharacterVO>) : State {

    companion object {
        fun initialState() = FavoritesState(list = emptyList())
    }
}
