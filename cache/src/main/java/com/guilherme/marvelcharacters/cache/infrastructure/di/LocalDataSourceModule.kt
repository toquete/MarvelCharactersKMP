package com.guilherme.marvelcharacters.cache.infrastructure.di

import com.guilherme.marvelcharacters.cache.CharacterLocalDataSource
import com.guilherme.marvelcharacters.cache.CharacterLocalDataSourceImpl
import com.guilherme.marvelcharacters.cache.NightModeLocalDataSource
import com.guilherme.marvelcharacters.cache.NightModeLocalDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class LocalDataSourceModule {

    @Binds
    abstract fun bindCharacterLocalDataSource(
        characterLocalDataSourceImpl: CharacterLocalDataSourceImpl
    ): CharacterLocalDataSource

    @Binds
    abstract fun bindNightModeLocalDataSource(
        nightModeLocalDataSourceImpl: NightModeLocalDataSourceImpl
    ): NightModeLocalDataSource
}