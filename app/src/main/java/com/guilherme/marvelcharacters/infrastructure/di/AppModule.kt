package com.guilherme.marvelcharacters.infrastructure.di

import android.content.Context
import androidx.room.Room
import com.guilherme.marvelcharacters.data.model.Character
import com.guilherme.marvelcharacters.data.repository.CharacterRepository
import com.guilherme.marvelcharacters.data.repository.PreferenceRepository
import com.guilherme.marvelcharacters.data.source.local.CharacterDatabase
import com.guilherme.marvelcharacters.data.source.remote.RetrofitFactory
import com.guilherme.marvelcharacters.ui.detail.DetailViewModel
import com.guilherme.marvelcharacters.ui.favorites.FavoritesViewModel
import com.guilherme.marvelcharacters.ui.home.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

private const val DATABASE = "character_database"
private const val DEFAULT_PREFERENCES = "default_preferences"

val appModule = module {
    single { Room.databaseBuilder(get(), CharacterDatabase::class.java, DATABASE).build() }
    single { get<CharacterDatabase>().characterDao() }
    single { RetrofitFactory.makeRetrofitService() }
    single { CharacterRepository(get(), get()) }
    single { get<Context>().getSharedPreferences(DEFAULT_PREFERENCES, Context.MODE_PRIVATE) }
    single { PreferenceRepository(get()) }
    viewModel { HomeViewModel(get(), get()) }
    viewModel { FavoritesViewModel(get()) }
    viewModel { (character: Character) -> DetailViewModel(character, get()) }
}