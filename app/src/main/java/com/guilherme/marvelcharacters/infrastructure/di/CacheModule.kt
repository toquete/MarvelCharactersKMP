package com.guilherme.marvelcharacters.infrastructure.di

import com.guilherme.marvelcharacters.cache.memory.CharacterMemoryCache
import com.guilherme.marvelcharacters.cache.memory.CharacterMemoryCacheImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CacheModule {

    @Provides
    @Singleton
    fun providesCharacterMemoryCache(): CharacterMemoryCache {
        return CharacterMemoryCacheImpl()
    }
}