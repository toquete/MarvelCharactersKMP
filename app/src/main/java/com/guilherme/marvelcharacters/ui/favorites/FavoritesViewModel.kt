package com.guilherme.marvelcharacters.ui.favorites

import android.database.sqlite.SQLiteException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guilherme.marvelcharacters.R
import com.guilherme.marvelcharacters.domain.usecase.DeleteAllFavoriteCharactersUseCase
import com.guilherme.marvelcharacters.domain.usecase.DeleteFavoriteCharacterUseCase
import com.guilherme.marvelcharacters.domain.usecase.GetFavoriteCharactersUseCase
import com.guilherme.marvelcharacters.model.CharacterVO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    getFavoriteCharactersUseCase: GetFavoriteCharactersUseCase,
    private val deleteFavoriteCharacterUseCase: DeleteFavoriteCharacterUseCase,
    private val deleteAllFavoriteCharactersUseCase: DeleteAllFavoriteCharactersUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(FavoritesState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            getFavoriteCharactersUseCase()
                .collect { list ->
                    _state.update { it.copy(list = list) }
                }
        }
    }

    fun deleteCharacter(character: CharacterVO) {
        viewModelScope.launch {
            deleteFavoriteCharacterUseCase(character.id)
        }
    }

    fun onDeleteAllClick() {
        viewModelScope.launch {
            try {
                deleteAllFavoriteCharactersUseCase()
                _state.update { it.copy(messageId = R.string.character_deleted) }
            } catch (exception: SQLiteException) {
                _state.update { it.copy(messageId = R.string.error_message) }
            }
        }
    }

    fun onErrorMessageShown() {
        _state.update { it.copy(messageId = null) }
    }
}
