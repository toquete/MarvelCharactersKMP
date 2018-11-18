package com.guilherme.marvelcharacters.ui.main

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import com.guilherme.marvelcharacters.BaseViewModel
import com.guilherme.marvelcharacters.data.model.Character
import com.guilherme.marvelcharacters.data.repository.CharacterRepository
import com.guilherme.marvelcharacters.data.repository.CharacterRequestException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MainViewModel(private val characterRepository: CharacterRepository) : BaseViewModel() {

    val isLoading = ObservableBoolean(false)

    val isEmpty = ObservableBoolean(true)

    val message = ObservableField<String>("Use the search box above!")

    private val _characters = MutableLiveData<List<Character>>()

    val characters: LiveData<List<Character>>
        get() = _characters

    override val uiScope: CoroutineScope
        get() = super.uiScope

    fun onSearchCharacter(character: String) {
        uiScope.launch {
            try {
                isLoading.set(true)
                isEmpty.set(false)
                val charactersList = characterRepository.getCharacters(character)
                _characters.value = charactersList

                if (charactersList.isEmpty()) {
                    message.set("No characters with that name. Try again!")
                    isEmpty.set(true)
                }
            } catch (error: CharacterRequestException) {
                message.set(error.message)
                isEmpty.set(true)
            } finally {
                isLoading.set(false)
            }
        }
    }
}