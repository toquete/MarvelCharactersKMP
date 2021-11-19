package com.guilherme.marvelcharacters.infrastructure.di

import com.guilherme.marvelcharacters.data.source.remote.CharacterRemoteDataSource
import com.guilherme.marvelcharacters.remote.CharacterRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class RemoteDataSourceModule {

    @Binds
    abstract fun bindsCharacterRemoteDataSource(
        characterRemoteDataSourceImpl: CharacterRemoteDataSourceImpl
    ): CharacterRemoteDataSource
}