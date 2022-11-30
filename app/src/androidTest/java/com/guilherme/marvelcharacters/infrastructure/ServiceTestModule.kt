package com.guilherme.marvelcharacters.infrastructure

import com.guilherme.marvelcharacters.remote.infrastructure.di.ServiceModule
import com.guilherme.marvelcharacters.remote.service.Api
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