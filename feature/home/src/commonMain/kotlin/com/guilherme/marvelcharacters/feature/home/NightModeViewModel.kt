package com.guilherme.marvelcharacters.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guilherme.marvelcharacters.core.model.DarkThemeConfig
import com.guilherme.marvelcharacters.domain.usecase.GetDarkModeUseCase
import com.guilherme.marvelcharacters.domain.usecase.IsDarkModeEnabledUseCase
import com.guilherme.marvelcharacters.domain.usecase.ToggleDarkModeUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NightModeViewModel(
    private val getDarkModeUseCase: GetDarkModeUseCase,
    private val toggleDarkModeUseCase: ToggleDarkModeUseCase,
    private val isDarkModeEnabledUseCase: IsDarkModeEnabledUseCase
) : ViewModel() {

    private val _nightMode = MutableStateFlow(DarkThemeConfig.LIGHT.ordinal)
    val nightMode: StateFlow<Int> = _nightMode

    init {
        viewModelScope.launch {
            _nightMode.value = getDarkModeUseCase()
        }
    }

    fun toggleDarkMode() {
        viewModelScope.launch {
            val isDarkModeEnabled = isDarkModeEnabledUseCase()
            toggleDarkModeUseCase(!isDarkModeEnabled)
            _nightMode.update { getDarkModeUseCase() }
        }
    }
}