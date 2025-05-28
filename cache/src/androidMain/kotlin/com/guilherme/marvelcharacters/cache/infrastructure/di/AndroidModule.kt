package com.guilherme.marvelcharacters.cache.infrastructure.di

import android.content.Context
import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.guilherme.marvelcharacters.cache.createDataStore
import com.guilherme.marvelcharacters.cache.dao.CharacterDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

private const val DATABASE = "character_database"

internal actual val platformModule: Module = module {
    single { appDatabase(androidContext()) }
    single { createDataStore(androidContext()) }
}

private fun appDatabase(context: Context): CharacterDatabase {
    val dbFile = context.getDatabasePath(DATABASE)
    return Room.databaseBuilder<CharacterDatabase>(context, dbFile.absolutePath)
        .setDriver(BundledSQLiteDriver())
        .build()
}