package com.guilherme.marvelcharacters.infrastructure.di

import androidx.lifecycle.SavedStateHandle
import androidx.room.Room
import com.guilherme.marvelcharacters.data.model.Character
import com.guilherme.marvelcharacters.data.repository.CharacterRepository
import com.guilherme.marvelcharacters.data.source.local.CharacterDatabase
import com.guilherme.marvelcharacters.data.source.remote.RetrofitFactory
import com.guilherme.marvelcharacters.ui.detail.DetailViewModel
import com.guilherme.marvelcharacters.ui.favorites.FavoritesViewModel
import com.guilherme.marvelcharacters.ui.home.HomeViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

private const val DATABASE = "character_database"

val appModule = module {
    single { Room.databaseBuilder(get(), CharacterDatabase::class.java, DATABASE).build() }
    single { get<CharacterDatabase>().characterDao() }
    single { RetrofitFactory.makeRetrofitService() }
    single { CharacterRepository(get(), get(), Dispatchers.IO) }
    viewModel { HomeViewModel(get(), Dispatchers.Main) }
    viewModel { FavoritesViewModel(get(), Dispatchers.Main) }
    viewModel { (character: Character) -> DetailViewModel(character, get(), Dispatchers.Main) }
}