package com.guilherme.marvelcharacters.remote.infrastructure.di

import com.guilherme.marvelcharacters.remote.CharacterRemoteDataSource
import com.guilherme.marvelcharacters.remote.CharacterRemoteDataSourceImpl
import com.guilherme.marvelcharacters.remote.service.KtorService
import org.koin.dsl.module

internal val commonModule = module {
    single { KtorService(engine = get()) }

    factory<CharacterRemoteDataSource> { CharacterRemoteDataSourceImpl(service = get()) }
}

val remoteModule = commonModule + platformModule