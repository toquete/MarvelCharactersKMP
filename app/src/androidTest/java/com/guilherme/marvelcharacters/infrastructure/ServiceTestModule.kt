package com.guilherme.marvelcharacters.infrastructure

import com.guilherme.marvelcharacters.data.source.remote.service.Api
import com.guilherme.marvelcharacters.infrastructure.di.ServiceModule
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import io.mockk.mockk
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [ServiceModule::class]
)
object ServiceTestModule {

    @Provides
    @Singleton
    fun providesRetrofitService(): Api {
        return mockk()
    }
}