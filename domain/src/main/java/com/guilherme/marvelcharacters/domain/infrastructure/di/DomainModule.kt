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
import org.koin.dsl.module

val domainModule = module {
    factory { DeleteAllFavoriteCharactersUseCase(characterRepository = get()) }
    factory { GetCharactersUseCase(characterRepository = get()) }
    factory { GetDarkModeUseCase(nightModeRepository = get()) }
    factory { GetFavoriteCharacterByIdUseCase(repository = get()) }
    factory { GetFavoriteCharactersUseCase(characterRepository = get()) }
    factory { IsCharacterFavoriteUseCase(characterRepository = get()) }
    factory { IsDarkModeEnabledUseCase(nightModeRepository = get()) }
    factory { ToggleDarkModeUseCase(nightModeRepository = get()) }
    factory { ToggleFavoriteCharacterUseCase(repository = get()) }
}