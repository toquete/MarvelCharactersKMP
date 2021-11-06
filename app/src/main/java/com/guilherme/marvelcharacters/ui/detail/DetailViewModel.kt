package com.guilherme.marvelcharacters.ui.detail

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

class DetailViewModel(
    private val character: Character,
    private val characterRepository: CharacterRepository
) : ViewModel() {

    private val _snackbarMessage = MutableLiveData<Event<Pair<Int, Boolean>>>()
    val snackbarMessage: LiveData<Event<Pair<Int, Boolean>>> = _snackbarMessage

    private val _isCharacterFavorite = MutableLiveData<Boolean>()
    val isCharacterFavorite: LiveData<Boolean> = _isCharacterFavorite

    init {
        viewModelScope.launch {
            _isCharacterFavorite.value = characterRepository.isCharacterFavorite(character.id)
        }
    }

    fun onFabClick() = viewModelScope.launch {
        try {
            isCharacterFavorite.value?.let { isFavorite ->
                if (isFavorite) {
                    characterRepository.deleteFavoriteCharacter(character)
                    _snackbarMessage.value = Event(R.string.character_deleted to true)
                } else {
                    characterRepository.insertFavoriteCharacter(character)
                    _snackbarMessage.value = Event(R.string.character_added to false)
                }
            } ?: run {
                _snackbarMessage.value = Event(R.string.unknown_character to false)
            }
        } catch (error: SQLiteException) {
            _snackbarMessage.value = Event(R.string.error_message to false)
        }
    }

    fun onUndoClick() = viewModelScope.launch {
        try {
            characterRepository.insertFavoriteCharacter(character)
        } catch (error: SQLiteException) {
            _snackbarMessage.value = Event(R.string.error_message to false)
        }
    }
}