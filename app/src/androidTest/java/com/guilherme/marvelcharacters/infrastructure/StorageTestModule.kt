package com.guilherme.marvelcharacters.infrastructure

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.guilherme.marvelcharacters.cache.dao.CharacterDatabase
import com.guilherme.marvelcharacters.infrastructure.di.StorageModule
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

private const val DEFAULT_PREFERENCES = "default_preferences"

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [StorageModule::class]
)
object StorageTestModule {

    @Provides
    @Singleton
    fun providesDatabase(@ApplicationContext context: Context): CharacterDatabase {
        return Room.inMemoryDatabaseBuilder(context, CharacterDatabase::class.java).build()
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(DEFAULT_PREFERENCES, Context.MODE_PRIVATE)
    }
}