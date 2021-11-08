package com.guilherme.marvelcharacters.infrastructure

import android.content.Context
import androidx.room.Room
import com.guilherme.marvelcharacters.data.repository.CharacterRepositoryImpl
import com.guilherme.marvelcharacters.data.repository.NightModeRepositoryImpl
import com.guilherme.marvelcharacters.data.source.local.CharacterLocalDataSource
import com.guilherme.marvelcharacters.data.source.local.CharacterLocalDataSourceImpl
import com.guilherme.marvelcharacters.data.source.local.NightModeLocalDataSource
import com.guilherme.marvelcharacters.data.source.local.NightModeLocalDataSourceImpl
import com.guilherme.marvelcharacters.data.source.remote.CharacterRemoteDataSource
import com.guilherme.marvelcharacters.data.source.remote.CharacterRemoteDataSourceImpl
import com.guilherme.marvelcharacters.data.source.remote.service.Api
import com.guilherme.marvelcharacters.domain.model.Character
import com.guilherme.marvelcharacters.domain.repository.CharacterRepository
import com.guilherme.marvelcharacters.domain.repository.NightModeRepository
import com.guilherme.marvelcharacters.domain.usecase.DeleteAllFavoriteCharactersUseCase
import com.guilherme.marvelcharacters.domain.usecase.DeleteFavoriteCharacterUseCase
import com.guilherme.marvelcharacters.domain.usecase.GetCharactersUseCase
import com.guilherme.marvelcharacters.domain.usecase.GetDarkModeUseCase
import com.guilherme.marvelcharacters.domain.usecase.GetFavoriteCharactersUseCase
import com.guilherme.marvelcharacters.domain.usecase.InsertFavoriteCharacterUseCase
import com.guilherme.marvelcharacters.domain.usecase.IsCharacterFavoriteUseCase
import com.guilherme.marvelcharacters.domain.usecase.IsDarkModeEnabledUseCase
import com.guilherme.marvelcharacters.domain.usecase.ToggleDarkModeUseCase
import com.guilherme.marvelcharacters.infrastructure.database.CharacterDatabase
import com.guilherme.marvelcharacters.infrastructure.util.Mapper
import com.guilherme.marvelcharacters.ui.detail.DetailViewModel
import com.guilherme.marvelcharacters.ui.favorites.FavoritesViewModel
import com.guilherme.marvelcharacters.ui.home.HomeViewModel
import com.guilherme.marvelcharacters.ui.mapper.CharacterMapper
import com.guilherme.marvelcharacters.ui.model.CharacterVO
import io.mockk.mockk
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

private const val DEFAULT_PREFERENCES = "default_preferences"

val testModule = module {
    single { mockk<Api>() }
    single { Room.inMemoryDatabaseBuilder(get(), CharacterDatabase::class.java).build() }
    single { get<CharacterDatabase>().characterDao() }
    single { get<Context>().getSharedPreferences(DEFAULT_PREFERENCES, Context.MODE_PRIVATE) }
    factory<Mapper<Character, CharacterVO>> { CharacterMapper() }
    factory<CharacterRemoteDataSource> { CharacterRemoteDataSourceImpl(get()) }
    factory<CharacterLocalDataSource> { CharacterLocalDataSourceImpl(get()) }
    factory<NightModeLocalDataSource> { NightModeLocalDataSourceImpl(get()) }
    factory<CharacterRepository> { CharacterRepositoryImpl(get(), get()) }
    factory<NightModeRepository> { NightModeRepositoryImpl(get()) }
    factory { GetCharactersUseCase(get()) }
    factory { IsCharacterFavoriteUseCase(get()) }
    factory { DeleteFavoriteCharacterUseCase(get()) }
    factory { InsertFavoriteCharacterUseCase(get()) }
    factory { DeleteAllFavoriteCharactersUseCase(get()) }
    factory { GetFavoriteCharactersUseCase(get()) }
    factory { GetDarkModeUseCase(get()) }
    factory { IsDarkModeEnabledUseCase(get()) }
    factory { ToggleDarkModeUseCase(get()) }
    viewModel { HomeViewModel(get(), get(), get(), get()) }
    viewModel { FavoritesViewModel(get(), get(), get()) }
    viewModel { (character: Character) -> DetailViewModel(character, get(), get(), get()) }
}