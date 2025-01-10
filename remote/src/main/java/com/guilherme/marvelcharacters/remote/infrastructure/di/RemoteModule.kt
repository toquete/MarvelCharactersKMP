package com.guilherme.marvelcharacters.remote.infrastructure.di

import com.guilherme.marvelcharacters.remote.CharacterRemoteDataSource
import com.guilherme.marvelcharacters.remote.CharacterRemoteDataSourceImpl
import com.guilherme.marvelcharacters.remote.service.Api
import com.guilherme.marvelcharacters.remote.service.RetrofitFactory
import kotlinx.serialization.ExperimentalSerializationApi
import org.koin.dsl.module

@OptIn(ExperimentalSerializationApi::class)
val remoteModule = module {
    single<Api> { RetrofitFactory.makeRetrofitService() }

    factory<CharacterRemoteDataSource> { CharacterRemoteDataSourceImpl(api = get()) }
}