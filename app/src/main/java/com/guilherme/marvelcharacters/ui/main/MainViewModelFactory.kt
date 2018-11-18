package com.guilherme.marvelcharacters.ui.main

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.guilherme.marvelcharacters.data.repository.CharacterRepository

class MainViewModelFactory(private val characterRepository: CharacterRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(characterRepository) as T
    }
}