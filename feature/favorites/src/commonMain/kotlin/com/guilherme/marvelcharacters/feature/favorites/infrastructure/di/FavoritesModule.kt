package com.guilherme.marvelcharacters.feature.favorites.infrastructure.di

import com.guilherme.marvelcharacters.feature.favorites.FavoritesViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val favoritesModule = module {
    viewModelOf(::FavoritesViewModel)
}