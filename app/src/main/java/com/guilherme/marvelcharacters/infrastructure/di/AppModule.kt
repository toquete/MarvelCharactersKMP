package com.guilherme.marvelcharacters.infrastructure.di

import com.guilherme.marvelcharacters.data.repository.CharacterRepository
import com.guilherme.marvelcharacters.data.repository.CharacterRepositoryImpl
import com.guilherme.marvelcharacters.data.source.remote.RetrofitFactory
import com.guilherme.marvelcharacters.ui.home.HomeViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import kotlin.coroutines.CoroutineContext

val appModule = module {
    single { RetrofitFactory.makeRetrofitService() }
    single<CharacterRepository> { CharacterRepositoryImpl(get()) }
    single<CoroutineContext> { Dispatchers.Main }
    viewModel { HomeViewModel(get(), get()) }
}