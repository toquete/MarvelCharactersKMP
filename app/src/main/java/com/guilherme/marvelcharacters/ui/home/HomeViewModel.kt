package com.guilherme.marvelcharacters.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guilherme.marvelcharacters.Event
import com.guilherme.marvelcharacters.data.model.Character
import com.guilherme.marvelcharacters.data.repository.CharacterRepository
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class HomeViewModel(
    private val characterRepository: CharacterRepository,
    private val coroutineContext: CoroutineContext
) : ViewModel() {

    private val _states = MutableLiveData<CharacterListState>()
    val states: LiveData<CharacterListState> = _states

    private val _navigateToDetail = MutableLiveData<Event<Character>>()
    val navigateToDetail: LiveData<Event<Character>> = _navigateToDetail

    var query: String? = null

    fun onSearchCharacter(character: String) {
        viewModelScope.launch(coroutineContext) {
            _states.value = CharacterListState.Loading
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

    fun onItemClick(character: Character) {
        _navigateToDetail.value = Event(character)
    }

    sealed class CharacterListState {
        data class Characters(val characters: List<Character>) : CharacterListState()
        data class ErrorState(val error: Exception) : CharacterListState()
        object EmptyState : CharacterListState()
        object Loading : CharacterListState()
    }
}