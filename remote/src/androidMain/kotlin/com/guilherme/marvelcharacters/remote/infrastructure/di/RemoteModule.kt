package com.guilherme.marvelcharacters.remote.infrastructure.di

import com.guilherme.marvelcharacters.remote.CharacterRemoteDataSource
import com.guilherme.marvelcharacters.remote.CharacterRemoteDataSourceImpl
import com.guilherme.marvelcharacters.remote.service.KtorService
import io.ktor.client.engine.*
import io.ktor.client.engine.okhttp.*
import org.koin.dsl.module

val remoteModule = module {
    single<HttpClientEngine> { OkHttpEngine(config = OkHttpConfig()) }
    single { KtorService(engine = get()) }

    factory<CharacterRemoteDataSource> { CharacterRemoteDataSourceImpl(service = get()) }
}