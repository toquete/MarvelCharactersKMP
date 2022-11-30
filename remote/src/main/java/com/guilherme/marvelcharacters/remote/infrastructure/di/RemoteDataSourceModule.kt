package com.guilherme.marvelcharacters.remote.infrastructure.di

import com.guilherme.marvelcharacters.remote.CharacterRemoteDataSource
import com.guilherme.marvelcharacters.remote.CharacterRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RemoteDataSourceModule {

    @Binds
    abstract fun bindsCharacterRemoteDataSource(
        characterRemoteDataSourceImpl: CharacterRemoteDataSourceImpl
    ): CharacterRemoteDataSource
}