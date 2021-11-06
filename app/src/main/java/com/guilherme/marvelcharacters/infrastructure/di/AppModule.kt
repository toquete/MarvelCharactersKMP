package com.guilherme.marvelcharacters.infrastructure.di

import android.content.Context
import androidx.room.Room
import com.guilherme.marvelcharacters.data.repository.CharacterRepositoryImpl
import com.guilherme.marvelcharacters.data.repository.PreferenceRepository
import com.guilherme.marvelcharacters.data.source.local.CharacterLocalDataSource
import com.guilherme.marvelcharacters.data.source.local.CharacterLocalDataSourceImpl
import com.guilherme.marvelcharacters.data.source.remote.CharacterRemoteDataSource
import com.guilherme.marvelcharacters.data.source.remote.CharacterRemoteDataSourceImpl
import com.guilherme.marvelcharacters.domain.model.Character
import com.guilherme.marvelcharacters.domain.repository.CharacterRepository
import com.guilherme.marvelcharacters.infrastructure.database.CharacterDatabase
import com.guilherme.marvelcharacters.infrastructure.service.RetrofitFactory
import com.guilherme.marvelcharacters.infrastructure.util.Mapper
import com.guilherme.marvelcharacters.ui.detail.DetailViewModel
import com.guilherme.marvelcharacters.ui.favorites.FavoritesViewModel
import com.guilherme.marvelcharacters.ui.home.HomeViewModel
import com.guilherme.marvelcharacters.ui.mapper.CharacterMapper
import com.guilherme.marvelcharacters.ui.model.CharacterVO
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

private const val DATABASE = "character_database"
private const val DEFAULT_PREFERENCES = "default_preferences"

val appModule = module {
    single { Room.databaseBuilder(get(), CharacterDatabase::class.java, DATABASE).build() }
    single { get<CharacterDatabase>().characterDao() }
    single { RetrofitFactory.makeRetrofitService() }
    factory<Mapper<Character, CharacterVO>> { CharacterMapper() }
    factory<CharacterRemoteDataSource> { CharacterRemoteDataSourceImpl(get()) }
    factory<CharacterLocalDataSource> { CharacterLocalDataSourceImpl(get()) }
    factory<CharacterRepository> { CharacterRepositoryImpl(get(), get()) }
    single { get<Context>().getSharedPreferences(DEFAULT_PREFERENCES, Context.MODE_PRIVATE) }
    single { PreferenceRepository(get()) }
    viewModel { HomeViewModel(get(), get()) }
    viewModel { FavoritesViewModel(get()) }
    viewModel { (character: Character) -> DetailViewModel(character, get()) }
}