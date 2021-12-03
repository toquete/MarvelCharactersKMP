package com.guilherme.marvelcharacters.ui.home

import androidx.annotation.StringRes
import com.guilherme.marvelcharacters.infrastructure.State
import com.guilherme.marvelcharacters.model.CharacterVO

data class HomeState(
    val isLoading: Boolean,
    val characters: List<CharacterVO>,
    @StringRes val errorMessageId: Int?
) : State {

    fun showLoading() = copy(isLoading = true)

    fun hideLoading() = copy(isLoading = false)

    companion object {
        fun initialState() = HomeState(
            isLoading = false,
            characters = listOf(),
            errorMessageId = null
        )
    }
}
