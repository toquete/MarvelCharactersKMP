package com.guilherme.marvelcharacters.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.guilherme.marvelcharacters.BaseViewModel
import com.guilherme.marvelcharacters.data.model.Character
import com.guilherme.marvelcharacters.data.repository.CharacterRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.lang.Exception
import kotlin.coroutines.CoroutineContext

class MainViewModel(
    private val characterRepository: CharacterRepository,
    coroutineContext: CoroutineContext
) : BaseViewModel(coroutineContext) {

    private val _states = MutableLiveData<CharacterListState>()
    val states: LiveData<CharacterListState>
        get() = _states

    private val _showLoading = MutableLiveData<Boolean>()
    val showLoading: LiveData<Boolean>
        get() = _showLoading

    override val uiScope: CoroutineScope
        get() = super.uiScope

    fun onSearchCharacter(character: String) {
        uiScope.launch {
            _showLoading.value = true
            try {
                val charactersList = characterRepository.getCharacters(character)
                _states.value = if (charactersList.isEmpty()) {
                    CharacterListState.EmptyState
                } else {
                    CharacterListState.Characters(charactersList)
                }
            } catch (error: Exception) {
                _states.value = CharacterListState.ErrorState(error)
            } finally {
                _showLoading.value = false
            }
        }
    }

    sealed class CharacterListState {
        data class Characters(val characters: List<Character>) : CharacterListState()
        data class ErrorState(val error: Exception) : CharacterListState()
        object EmptyState : CharacterListState()
    }
}