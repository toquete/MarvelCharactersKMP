package com.guilherme.marvelcharacters.infrastructure.di

import com.guilherme.marvelcharacters.cache.CharacterLocalDataSourceImpl
import com.guilherme.marvelcharacters.cache.NightModeLocalDataSourceImpl
import com.guilherme.marvelcharacters.data.source.local.CharacterLocalDataSource
import com.guilherme.marvelcharacters.data.source.local.NightModeLocalDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class LocalDataSourceModule {

    @Binds
    abstract fun bindCharacterLocalDataSource(
        characterLocalDataSourceImpl: CharacterLocalDataSourceImpl
    ): CharacterLocalDataSource

    @Binds
    abstract fun bindNightModeLocalDataSource(
        nightModeLocalDataSourceImpl: NightModeLocalDataSourceImpl
    ): NightModeLocalDataSource
}