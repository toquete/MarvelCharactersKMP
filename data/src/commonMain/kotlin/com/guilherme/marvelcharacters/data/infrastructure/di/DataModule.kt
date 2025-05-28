package com.guilherme.marvelcharacters.data.infrastructure.di

import com.guilherme.marvelcharacters.data.repository.CharacterRepository
import com.guilherme.marvelcharacters.data.repository.CharacterRepositoryImpl
import com.guilherme.marvelcharacters.data.repository.NightModeRepository
import com.guilherme.marvelcharacters.data.repository.NightModeRepositoryImpl
import org.koin.dsl.module

val dataModule = module {
    factory<CharacterRepository> {
        CharacterRepositoryImpl(
            remoteDataSource = get(),
            localDataSource = get(),
            favoriteLocalDataSource = get()
        )
    }
    factory<NightModeRepository> { NightModeRepositoryImpl(nightModeLocalDataSource = get()) }
}