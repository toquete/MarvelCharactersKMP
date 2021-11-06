package com.guilherme.marvelcharacters.ui.detail

import android.database.sqlite.SQLiteException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guilherme.marvelcharacters.Event
import com.guilherme.marvelcharacters.R
import com.guilherme.marvelcharacters.domain.model.Character
import com.guilherme.marvelcharacters.domain.usecase.DeleteFavoriteCharacterUseCase
import com.guilherme.marvelcharacters.domain.usecase.InsertFavoriteCharacterUseCase
import com.guilherme.marvelcharacters.domain.usecase.IsCharacterFavoriteUseCase
import kotlinx.coroutines.launch

class DetailViewModel(
    private val character: Character,
    private val isCharacterFavoriteUseCase: IsCharacterFavoriteUseCase,
    private val deleteFavoriteCharacterUseCase: DeleteFavoriteCharacterUseCase,
    private val insertFavoriteCharacterUseCase: InsertFavoriteCharacterUseCase
) : ViewModel() {

    private val _snackbarMessage = MutableLiveData<Event<Pair<Int, Boolean>>>()
    val snackbarMessage: LiveData<Event<Pair<Int, Boolean>>> = _snackbarMessage

    private val _isCharacterFavorite = MutableLiveData<Boolean>()
    val isCharacterFavorite: LiveData<Boolean> = _isCharacterFavorite

    init {
        viewModelScope.launch {
            _isCharacterFavorite.value = isCharacterFavoriteUseCase(character.id)
        }
    }

    fun onFabClick() = viewModelScope.launch {
        try {
            isCharacterFavorite.value?.let { isFavorite ->
                if (isFavorite) {
                    deleteFavoriteCharacterUseCase(character)
                    _snackbarMessage.value = Event(R.string.character_deleted to true)
                } else {
                    insertFavoriteCharacterUseCase(character)
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
            insertFavoriteCharacterUseCase(character)
        } catch (error: SQLiteException) {
            _snackbarMessage.value = Event(R.string.error_message to false)
        }
    }
}