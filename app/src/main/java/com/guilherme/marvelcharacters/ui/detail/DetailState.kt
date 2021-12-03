package com.guilherme.marvelcharacters.ui.detail

import com.guilherme.marvelcharacters.infrastructure.State

data class DetailState(val isFavorite: Boolean) : State {

    companion object {
        fun initialState() = DetailState(isFavorite = false)
    }
}
