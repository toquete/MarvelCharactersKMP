package com.guilherme.marvelcharacters.cache.infrastructure.di

import android.content.Context
import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.guilherme.marvelcharacters.cache.CharacterLocalDataSource
import com.guilherme.marvelcharacters.cache.CharacterLocalDataSourceImpl
import com.guilherme.marvelcharacters.cache.DataStoreNightModeLocalDataSource
import com.guilherme.marvelcharacters.cache.FavoriteCharacterLocalDataSource
import com.guilherme.marvelcharacters.cache.FavoriteCharacterLocalDataSourceImpl
import com.guilherme.marvelcharacters.cache.NightModeLocalDataSource
import com.guilherme.marvelcharacters.cache.createDataStore
import com.guilherme.marvelcharacters.cache.dao.CharacterDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

private const val DATABASE = "character_database"

val cacheModule = module {
    single { appDatabase(androidContext()) }
    single { createDataStore(androidContext()) }

    factory { get<CharacterDatabase>().characterDao() }
    factory { get<CharacterDatabase>().favoriteCharacterDao() }

    factory<NightModeLocalDataSource> { DataStoreNightModeLocalDataSource(dataStore = get()) }
    factory<CharacterLocalDataSource> { CharacterLocalDataSourceImpl(dao = get()) }
    factory<FavoriteCharacterLocalDataSource> { FavoriteCharacterLocalDataSourceImpl(dao = get()) }
}

internal fun appDatabase(context: Context): CharacterDatabase {
    val dbFile = context.getDatabasePath(DATABASE)
    return Room.databaseBuilder<CharacterDatabase>(context, dbFile.absolutePath)
        .setDriver(BundledSQLiteDriver())
        .build()
}