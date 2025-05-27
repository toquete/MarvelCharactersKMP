package com.guilherme.marvelcharacters.remote.infrastructure.di

import com.guilherme.marvelcharacters.remote.CharacterRemoteDataSource
import com.guilherme.marvelcharacters.remote.CharacterRemoteDataSourceImpl
import com.guilherme.marvelcharacters.remote.service.KtorService
import com.guilherme.marvelcharacters.remote.service.Service
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

internal val commonModule = module {
    singleOf(::KtorService).bind<Service>()
    factoryOf(::CharacterRemoteDataSourceImpl).bind<CharacterRemoteDataSource>()
}

val remoteModule = commonModule + platformModule