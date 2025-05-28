package com.guilherme.marvelcharacters.cache.infrastructure.di

import com.guilherme.marvelcharacters.cache.CharacterLocalDataSource
import com.guilherme.marvelcharacters.cache.CharacterLocalDataSourceImpl
import com.guilherme.marvelcharacters.cache.DataStoreNightModeLocalDataSource
import com.guilherme.marvelcharacters.cache.FavoriteCharacterLocalDataSource
import com.guilherme.marvelcharacters.cache.FavoriteCharacterLocalDataSourceImpl
import com.guilherme.marvelcharacters.cache.NightModeLocalDataSource
import com.guilherme.marvelcharacters.cache.dao.CharacterDatabase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

internal val commonModule = module {
    factory { get<CharacterDatabase>().characterDao() }
    factory { get<CharacterDatabase>().favoriteCharacterDao() }

    factoryOf(::DataStoreNightModeLocalDataSource).bind<NightModeLocalDataSource>()
    factoryOf(::CharacterLocalDataSourceImpl).bind<CharacterLocalDataSource>()
    factoryOf(::FavoriteCharacterLocalDataSourceImpl).bind<FavoriteCharacterLocalDataSource>()
}

val cacheModule = commonModule + platformModule
