package com.guilherme.marvelcharacters.infrastructure

import com.guilherme.marvelcharacters.data.repository.CharacterRepository
import com.guilherme.marvelcharacters.data.repository.CharacterRepositoryImpl
import com.guilherme.marvelcharacters.data.source.remote.Api
import com.guilherme.marvelcharacters.ui.home.HomeViewModel
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import kotlin.coroutines.CoroutineContext

val testModule = module {
    single { mockk<Api>() }
    single<CharacterRepository> { CharacterRepositoryImpl(get()) }
    single<CoroutineContext> { Dispatchers.Main }
    viewModel { HomeViewModel(get(), get()) }
}