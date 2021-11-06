package com.guilherme.marvelcharacters.ui.favorites

import android.database.sqlite.SQLiteException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guilherme.marvelcharacters.Event
import com.guilherme.marvelcharacters.R
import com.guilherme.marvelcharacters.data.repository.CharacterRepository
import com.guilherme.marvelcharacters.domain.model.Character
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val characterRepository: CharacterRepository
) : ViewModel() {

    private val _snackbarMessage = MutableLiveData<Event<Int>>()
    val snackbarMessage: LiveData<Event<Int>> = _snackbarMessage

    private val _navigateToDetail = MutableLiveData<Event<Character>>()
    val navigateToDetail: LiveData<Event<Character>> = _navigateToDetail

    private val _list = MutableLiveData<List<Character>>()
    val list: LiveData<List<Character>> = _list

    init {
        viewModelScope.launch {
            _list.value = characterRepository.getFavoriteCharacters()
        }
    }

    fun deleteCharacter(character: Character) = viewModelScope.launch {
        characterRepository.deleteFavoriteCharacter(character)
    }

    fun onDeleteAllClick() = viewModelScope.launch {
        try {
            characterRepository.deleteAllFavoriteCharacters()
            _snackbarMessage.value = Event(R.string.character_deleted)
        } catch (exception: SQLiteException) {
            _snackbarMessage.value = Event(R.string.error_message)
        }
    }

    fun onFavoriteItemClick(character: Character) {
        _navigateToDetail.value = Event(character)
    }
}
