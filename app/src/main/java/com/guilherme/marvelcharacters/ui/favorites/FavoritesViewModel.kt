package com.guilherme.marvelcharacters.ui.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guilherme.marvelcharacters.Event
import com.guilherme.marvelcharacters.R
import com.guilherme.marvelcharacters.data.model.Character
import com.guilherme.marvelcharacters.data.repository.CharacterRepository
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class FavoritesViewModel(
    private val characterRepository: CharacterRepository,
    private val coroutineContext: CoroutineContext
) : ViewModel() {

    private val _snackbarMessage = MutableLiveData<Event<Int>>()
    val snackbarMessage: LiveData<Event<Int>> = _snackbarMessage

    private val _navigateToDetail = MutableLiveData<Event<Character>>()
    val navigateToDetail: LiveData<Event<Character>> = _navigateToDetail

    val list: LiveData<List<Character>> = characterRepository.getFavoriteCharacters()

    fun deleteCharacter(character: Character) = viewModelScope.launch(coroutineContext) {
        characterRepository.deleteFavoriteCharacter(character)
    }

    fun onDeleteAllClick() = viewModelScope.launch(coroutineContext) {
        try {
            characterRepository.deleteAllFavoriteCharacters()
            _snackbarMessage.value = Event(R.string.character_deleted)
        } catch (exception: Exception) {
            _snackbarMessage.value = Event(R.string.error_message)
        }
    }

    fun onFavoriteItemClick(character: Character) {
        _navigateToDetail.value = Event(character)
    }
}
