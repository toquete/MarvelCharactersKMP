package com.guilherme.marvelcharacters.infrastructure.di

import com.guilherme.marvelcharacters.cache.dao.CharacterDao
import com.guilherme.marvelcharacters.cache.dao.CharacterDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DaoModule {

    @Provides
    fun providesCharacterDao(characterDatabase: CharacterDatabase): CharacterDao {
        return characterDatabase.characterDao()
    }
}