package com.guilherme.marvelcharacters.infrastructure

import android.content.Context
import androidx.room.Room
import com.guilherme.marvelcharacters.data.model.Character
import com.guilherme.marvelcharacters.data.repository.CharacterRepository
import com.guilherme.marvelcharacters.data.repository.PreferenceRepository
import com.guilherme.marvelcharacters.data.source.local.CharacterDatabase
import com.guilherme.marvelcharacters.data.source.remote.Api
import com.guilherme.marvelcharacters.ui.detail.DetailViewModel
import com.guilherme.marvelcharacters.ui.favorites.FavoritesViewModel
import com.guilherme.marvelcharacters.ui.home.HomeViewModel
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

private const val DEFAULT_PREFERENCES = "default_preferences"

val testModule = module {
    single { mockk<Api>() }
    single { Room.inMemoryDatabaseBuilder(get(), CharacterDatabase::class.java).build() }
    single { get<CharacterDatabase>().characterDao() }
    single { CharacterRepository(get(), get(), Dispatchers.IO) }
    single { get<Context>().getSharedPreferences(DEFAULT_PREFERENCES, Context.MODE_PRIVATE) }
    single { PreferenceRepository(get()) }
    viewModel { HomeViewModel(get(), get(), Dispatchers.Main) }
    viewModel { FavoritesViewModel(get(), Dispatchers.Main) }
    viewModel { (character: Character) -> DetailViewModel(character, get(), Dispatchers.Main) }
}