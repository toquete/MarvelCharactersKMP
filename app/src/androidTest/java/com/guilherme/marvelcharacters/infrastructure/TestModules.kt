package com.guilherme.marvelcharacters.infrastructure

import androidx.lifecycle.SavedStateHandle
import androidx.room.Room
import com.guilherme.marvelcharacters.data.repository.CharacterRepository
import com.guilherme.marvelcharacters.data.source.local.CharacterDatabase
import com.guilherme.marvelcharacters.data.source.remote.Api
import com.guilherme.marvelcharacters.ui.home.HomeViewModel
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val testModule = module {
    single { mockk<Api>() }
    single { Room.inMemoryDatabaseBuilder(get(), CharacterDatabase::class.java).build() }
    single { get<CharacterDatabase>().characterDao() }
    single { CharacterRepository(get(), get(), Dispatchers.IO) }
    viewModel { (handler: SavedStateHandle) -> HomeViewModel(get(), get(), handler) }
}