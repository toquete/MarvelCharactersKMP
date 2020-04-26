package com.guilherme.marvelcharacters.ui.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guilherme.marvelcharacters.Event
import com.guilherme.marvelcharacters.data.model.Character
import com.guilherme.marvelcharacters.data.repository.CharacterRepository
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class FavoritesViewModel(
    private val characterRepository: CharacterRepository,
    private val coroutineContext: CoroutineContext
) : ViewModel() {

    private val _state = MutableLiveData<FavoritesState>()
    val state: LiveData<FavoritesState> = _state

    private val _navigateToDetail = MutableLiveData<Event<Character>>()
    val navigateToDetail: LiveData<Event<Character>> = _navigateToDetail

    val list: LiveData<List<Character>> = characterRepository.getFavoriteCharacters()

    fun deleteCharacter(character: Character) = viewModelScope.launch(coroutineContext) {
        characterRepository.deleteFavoriteCharacter(character)
    }

    fun onDeleteAllClick() = viewModelScope.launch(coroutineContext) {
        try {
            characterRepository.deleteAllFavoriteCharacters()
            _state.value = FavoritesState.CharactersDeleted
        } catch (exception: Exception) {
            _state.value = FavoritesState.Error(exception)
        }
    }

    fun onFavoriteItemClick(character: Character) {
        _navigateToDetail.value = Event(character)
    }

    sealed class FavoritesState {
        object CharactersDeleted : FavoritesState()
        data class Error(val error: Exception) : FavoritesState()
    }
}
