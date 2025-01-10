package com.guilherme.marvelcharacters.feature.favorites.infrastructure.di

import com.guilherme.marvelcharacters.feature.favorites.FavoritesViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val favoritesModule = module {
    viewModel {
        FavoritesViewModel(
            getFavoriteCharactersUseCase = get(),
            deleteAllFavoriteCharactersUseCase = get()
        )
    }
}