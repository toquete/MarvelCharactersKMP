package com.guilherme.marvelcharacters.ui.favorites

import android.database.sqlite.SQLiteException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guilherme.marvelcharacters.Event
import com.guilherme.marvelcharacters.R
import com.guilherme.marvelcharacters.domain.usecase.DeleteAllFavoriteCharactersUseCase
import com.guilherme.marvelcharacters.domain.usecase.DeleteFavoriteCharacterUseCase
import com.guilherme.marvelcharacters.domain.usecase.GetFavoriteCharactersUseCase
import com.guilherme.marvelcharacters.ui.mapper.CharacterMapper
import com.guilherme.marvelcharacters.ui.model.CharacterVO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    getFavoriteCharactersUseCase: GetFavoriteCharactersUseCase,
    private val deleteFavoriteCharacterUseCase: DeleteFavoriteCharacterUseCase,
    private val deleteAllFavoriteCharactersUseCase: DeleteAllFavoriteCharactersUseCase,
    private val mapper: CharacterMapper
) : ViewModel() {

    private val _snackbarMessage = MutableLiveData<Event<Int>>()
    val snackbarMessage: LiveData<Event<Int>> = _snackbarMessage

    private val _navigateToDetail = MutableLiveData<Event<CharacterVO>>()
    val navigateToDetail: LiveData<Event<CharacterVO>> = _navigateToDetail

    val list: StateFlow<List<CharacterVO>> = getFavoriteCharactersUseCase()
        .map { list ->
            list.map { mapper.mapTo(it) }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = emptyList()
        )

    fun deleteCharacter(character: CharacterVO) = viewModelScope.launch {
        deleteFavoriteCharacterUseCase(mapper.mapFrom(character))
    }

    fun onDeleteAllClick() = viewModelScope.launch {
        try {
            deleteAllFavoriteCharactersUseCase()
            _snackbarMessage.value = Event(R.string.character_deleted)
        } catch (exception: SQLiteException) {
            _snackbarMessage.value = Event(R.string.error_message)
        }
    }

    fun onFavoriteItemClick(character: CharacterVO) {
        _navigateToDetail.value = Event(character)
    }
}
