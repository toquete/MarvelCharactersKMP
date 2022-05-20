package com.guilherme.marvelcharacters.ui.detail

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.guilherme.marvelcharacters.domain.model.Character
import com.guilherme.marvelcharacters.infrastructure.di.ViewModelFactoryProvider
import dagger.assisted.AssistedFactory
import dagger.hilt.android.EntryPointAccessors

@AssistedFactory
interface DetailViewModelFactory {
    fun create(character: Character): DetailViewModel
}

fun provideFactory(
    factory: DetailViewModelFactory,
    character: Character
): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return factory.create(character) as T
    }
}

@Composable
fun detailViewModel(character: Character): DetailViewModel {
    val factory = EntryPointAccessors.fromActivity(
        LocalContext.current as Activity,
        ViewModelFactoryProvider::class.java
    ).detailViewModelFactory()

    return viewModel(factory = provideFactory(factory, character))
}