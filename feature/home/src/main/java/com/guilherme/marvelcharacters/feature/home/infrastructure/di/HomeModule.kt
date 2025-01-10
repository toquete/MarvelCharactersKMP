package com.guilherme.marvelcharacters.feature.home.infrastructure.di

import com.guilherme.marvelcharacters.feature.home.HomeViewModel
import com.guilherme.marvelcharacters.feature.home.NightModeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val homeModule = module {
    viewModel { HomeViewModel(getCharactersUseCase = get()) }
    viewModel {
        NightModeViewModel(
            getDarkModeUseCase = get(),
            toggleDarkModeUseCase = get(),
            isDarkModeEnabledUseCase = get()
        )
    }
}