package com.guilherme.marvelcharacters.cache.infrastructure.di

import com.guilherme.marvelcharacters.cache.dao.CharacterDao
import com.guilherme.marvelcharacters.cache.dao.CharacterDatabase
import com.guilherme.marvelcharacters.cache.dao.FavoriteCharacterDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal object DaoModule {

    @Provides
    fun providesCharacterDao(characterDatabase: CharacterDatabase): CharacterDao {
        return characterDatabase.characterDao()
    }

    @Provides
    fun providesFavoriteCharacterDao(characterDatabase: CharacterDatabase): FavoriteCharacterDao {
        return characterDatabase.favoriteCharacterDao()
    }
}