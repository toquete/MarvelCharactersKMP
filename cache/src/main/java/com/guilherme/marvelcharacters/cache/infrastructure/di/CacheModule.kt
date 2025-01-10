package com.guilherme.marvelcharacters.cache.infrastructure.di

import android.content.Context
import androidx.room.Room
import com.guilherme.marvelcharacters.cache.CharacterLocalDataSource
import com.guilherme.marvelcharacters.cache.CharacterLocalDataSourceImpl
import com.guilherme.marvelcharacters.cache.FavoriteCharacterLocalDataSource
import com.guilherme.marvelcharacters.cache.FavoriteCharacterLocalDataSourceImpl
import com.guilherme.marvelcharacters.cache.NightModeLocalDataSource
import com.guilherme.marvelcharacters.cache.NightModeLocalDataSourceImpl
import com.guilherme.marvelcharacters.cache.dao.CharacterDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

private const val DATABASE = "character_database"
private const val DEFAULT_PREFERENCES = "default_preferences"

val cacheModule = module {
    single { Room.databaseBuilder(androidContext(), CharacterDatabase::class.java, DATABASE).build() }
    single { androidContext().getSharedPreferences(DEFAULT_PREFERENCES, Context.MODE_PRIVATE) }

    factory { get<CharacterDatabase>().characterDao() }
    factory { get<CharacterDatabase>().favoriteCharacterDao() }

    factory<CharacterLocalDataSource> { CharacterLocalDataSourceImpl(dao = get()) }
    factory<FavoriteCharacterLocalDataSource> { FavoriteCharacterLocalDataSourceImpl(dao = get()) }
    factory<NightModeLocalDataSource> { NightModeLocalDataSourceImpl(sharedPreferences = get()) }
}