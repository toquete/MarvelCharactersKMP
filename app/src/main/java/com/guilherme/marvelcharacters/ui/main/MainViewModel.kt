package com.guilherme.marvelcharacters.ui.main

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
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

    override val uiScope: CoroutineScope
        get() = super.uiScope

    fun onSearchCharacter(character: String) {
        uiScope.launch {
            _states.value = CharacterListState.LoadingState
            try {
                val charactersList = characterRepository.getCharacters(character)
                _states.value = if (charactersList.isEmpty()) {
                    CharacterListState.EmptyState
                } else {
                    CharacterListState.Characters(charactersList)
                }
            } catch (error: Exception) {
                _states.value = CharacterListState.ErrorState(error)
            }
        }
    }

    sealed class CharacterListState {
        data class Characters(val characters: List<Character>) : CharacterListState()
        data class ErrorState(val error: Exception) : CharacterListState()
        object EmptyState : CharacterListState()
        object LoadingState : CharacterListState()
    }
}