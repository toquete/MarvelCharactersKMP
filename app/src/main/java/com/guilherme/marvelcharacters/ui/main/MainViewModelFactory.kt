package com.guilherme.marvelcharacters.ui.main

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.guilherme.marvelcharacters.data.repository.CharacterRepository
import kotlin.coroutines.CoroutineContext

class MainViewModelFactory(
    private val characterRepository: CharacterRepository,
    private val coroutineContext: CoroutineContext
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(characterRepository, coroutineContext) as T
    }
}