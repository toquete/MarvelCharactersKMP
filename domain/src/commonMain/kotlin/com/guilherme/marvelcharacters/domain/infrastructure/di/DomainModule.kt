package com.guilherme.marvelcharacters.domain.infrastructure.di

import com.guilherme.marvelcharacters.domain.usecase.DeleteAllFavoriteCharactersUseCase
import com.guilherme.marvelcharacters.domain.usecase.GetCharactersUseCase
import com.guilherme.marvelcharacters.domain.usecase.GetDarkModeUseCase
import com.guilherme.marvelcharacters.domain.usecase.GetFavoriteCharacterByIdUseCase
import com.guilherme.marvelcharacters.domain.usecase.GetFavoriteCharactersUseCase
import com.guilherme.marvelcharacters.domain.usecase.IsCharacterFavoriteUseCase
import com.guilherme.marvelcharacters.domain.usecase.IsDarkModeEnabledUseCase
import com.guilherme.marvelcharacters.domain.usecase.ToggleDarkModeUseCase
import com.guilherme.marvelcharacters.domain.usecase.ToggleFavoriteCharacterUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val domainModule = module {
    factoryOf(::DeleteAllFavoriteCharactersUseCase)
    factoryOf(::GetCharactersUseCase)
    factoryOf(::GetDarkModeUseCase)
    factoryOf(::GetFavoriteCharacterByIdUseCase)
    factoryOf(::GetFavoriteCharactersUseCase)
    factoryOf(::IsCharacterFavoriteUseCase)
    factoryOf(::IsDarkModeEnabledUseCase)
    factoryOf(::ToggleDarkModeUseCase)
    factoryOf(::ToggleFavoriteCharacterUseCase)
}