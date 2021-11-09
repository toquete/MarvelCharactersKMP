package com.guilherme.marvelcharacters.data.source.local.di

import com.guilherme.marvelcharacters.data.source.local.CharacterLocalDataSource
import com.guilherme.marvelcharacters.data.source.local.CharacterLocalDataSourceImpl
import com.guilherme.marvelcharacters.data.source.local.NightModeLocalDataSource
import com.guilherme.marvelcharacters.data.source.local.NightModeLocalDataSourceImpl
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