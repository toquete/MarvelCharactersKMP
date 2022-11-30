package com.guilherme.marvelcharacters.remote.infrastructure.di

import com.guilherme.marvelcharacters.remote.service.Api
import com.guilherme.marvelcharacters.remote.service.RetrofitFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object ServiceModule {

    @Provides
    @Singleton
    fun providesRetrofitService(): Api {
        return RetrofitFactory.makeRetrofitService()
    }
}