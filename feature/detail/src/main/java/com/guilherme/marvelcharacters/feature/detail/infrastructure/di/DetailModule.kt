package com.guilherme.marvelcharacters.feature.detail.infrastructure.di

import com.guilherme.marvelcharacters.feature.detail.DetailViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val detailModule = module {
    viewModel {
        DetailViewModel(
            savedStateHandle = get(),
            getFavoriteCharacterByIdUseCase = get(),
            toggleFavoriteCharacterUseCase = get()
        )
    }
}