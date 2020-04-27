package com.guilherme.marvelcharacters.infrastructure

import com.guilherme.marvelcharacters.data.model.Character
import com.guilherme.marvelcharacters.data.repository.CharacterRepository
import com.guilherme.marvelcharacters.data.source.local.dao.CharacterDao
import com.guilherme.marvelcharacters.data.source.remote.Api
import com.guilherme.marvelcharacters.ui.detail.DetailViewModel
import com.guilherme.marvelcharacters.ui.favorites.FavoritesViewModel
import com.guilherme.marvelcharacters.ui.home.HomeViewModel
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val testModule = module {
    single { mockk<Api>() }
    single { mockk<CharacterDao>() }
    single { CharacterRepository(get(), get(), Dispatchers.IO) }
    viewModel { HomeViewModel(get(), Dispatchers.Main) }
    viewModel { FavoritesViewModel(get(), Dispatchers.Main) }
    viewModel { (character: Character) -> DetailViewModel(character, get(), Dispatchers.Main) }
}