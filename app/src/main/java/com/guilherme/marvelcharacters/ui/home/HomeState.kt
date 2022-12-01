package com.guilherme.marvelcharacters.ui.home

import androidx.annotation.StringRes
import com.guilherme.marvelcharacters.core.model.Character
import com.guilherme.marvelcharacters.infrastructure.State

data class HomeState(
    val isLoading: Boolean,
    val characters: List<Character>,
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
