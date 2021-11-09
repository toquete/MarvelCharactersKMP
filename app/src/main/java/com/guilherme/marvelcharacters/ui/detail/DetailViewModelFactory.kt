package com.guilherme.marvelcharacters.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.guilherme.marvelcharacters.domain.model.Character
import dagger.assisted.AssistedFactory

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