package com.guilherme.marvelcharacters.ui.favorites

import android.database.sqlite.SQLiteException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guilherme.marvelcharacters.Event
import com.guilherme.marvelcharacters.R
import com.guilherme.marvelcharacters.domain.model.Character
import com.guilherme.marvelcharacters.domain.usecase.DeleteAllFavoriteCharactersUseCase
import com.guilherme.marvelcharacters.domain.usecase.DeleteFavoriteCharacterUseCase
import com.guilherme.marvelcharacters.domain.usecase.GetFavoriteCharactersUseCase
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val getFavoriteCharactersUseCase: GetFavoriteCharactersUseCase,
    private val deleteFavoriteCharacterUseCase: DeleteFavoriteCharacterUseCase,
    private val deleteAllFavoriteCharactersUseCase: DeleteAllFavoriteCharactersUseCase
) : ViewModel() {

    private val _snackbarMessage = MutableLiveData<Event<Int>>()
    val snackbarMessage: LiveData<Event<Int>> = _snackbarMessage

    private val _navigateToDetail = MutableLiveData<Event<Character>>()
    val navigateToDetail: LiveData<Event<Character>> = _navigateToDetail

    private val _list = MutableLiveData<List<Character>>()
    val list: LiveData<List<Character>> = _list

    init {
        viewModelScope.launch {
            _list.value = getFavoriteCharactersUseCase()
        }
    }

    fun deleteCharacter(character: Character) = viewModelScope.launch {
        deleteFavoriteCharacterUseCase(character)
    }

    fun onDeleteAllClick() = viewModelScope.launch {
        try {
            deleteAllFavoriteCharactersUseCase()
            _snackbarMessage.value = Event(R.string.character_deleted)
        } catch (exception: SQLiteException) {
            _snackbarMessage.value = Event(R.string.error_message)
        }
    }

    fun onFavoriteItemClick(character: Character) {
        _navigateToDetail.value = Event(character)
    }
}
