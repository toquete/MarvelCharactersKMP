package com.guilherme.marvelcharacters.ui.favorites

import com.guilherme.marvelcharacters.core.model.Character
import com.guilherme.marvelcharacters.infrastructure.State

data class FavoritesState(val list: List<Character>) : State {

    companion object {
        fun initialState() = FavoritesState(list = emptyList())
    }
}
