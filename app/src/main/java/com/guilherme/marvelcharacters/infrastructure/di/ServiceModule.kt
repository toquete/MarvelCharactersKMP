package com.guilherme.marvelcharacters.infrastructure.di

import com.guilherme.marvelcharacters.remote.service.Api
import com.guilherme.marvelcharacters.remote.service.RetrofitFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {

    @Provides
    @Singleton
    fun providesRetrofitService(): Api {
        return RetrofitFactory.makeRetrofitService()
    }
}