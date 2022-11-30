package com.guilherme.marvelcharacters.cache.infrastructure.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.guilherme.marvelcharacters.cache.dao.CharacterDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private const val DATABASE = "character_database"
private const val DEFAULT_PREFERENCES = "default_preferences"

@Module
@InstallIn(SingletonComponent::class)
internal object StorageModule {

    @Provides
    @Singleton
    fun providesDatabase(@ApplicationContext context: Context): CharacterDatabase {
        return Room.databaseBuilder(
            context,
            CharacterDatabase::class.java,
            DATABASE
        ).build()
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(DEFAULT_PREFERENCES, Context.MODE_PRIVATE)
    }
}