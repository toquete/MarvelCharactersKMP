package com.guilherme.marvelcharacters.ui.detail

import android.database.sqlite.SQLiteException
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guilherme.marvelcharacters.R
import com.guilherme.marvelcharacters.domain.model.Character
import com.guilherme.marvelcharacters.domain.usecase.DeleteFavoriteCharacterUseCase
import com.guilherme.marvelcharacters.domain.usecase.GetCharacterByIdUseCase
import com.guilherme.marvelcharacters.domain.usecase.InsertFavoriteCharacterUseCase
import com.guilherme.marvelcharacters.domain.usecase.IsCharacterFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailComposeViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getCharacterByIdUseCase: GetCharacterByIdUseCase,
    isCharacterFavoriteUseCase: IsCharacterFavoriteUseCase,
    private val deleteFavoriteCharacterUseCase: DeleteFavoriteCharacterUseCase,
    private val insertFavoriteCharacterUseCase: InsertFavoriteCharacterUseCase,
) : ViewModel() {

    private val characterId: Int = checkNotNull(savedStateHandle["characterId"])

    private val _state = MutableStateFlow(DetailComposeState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                getCharacterByIdUseCase(characterId),
                isCharacterFavoriteUseCase(characterId)
            ) { character, isFavorite ->
                character.copy(isFavorite = isFavorite)
            }.collect { character ->
                _state.update { it.copy(character = character) }
            }
        }
    }

    fun onFabClick(character: Character) {
        viewModelScope.launch {
            try {
                if (character.isFavorite) {
                    deleteFavoriteCharacterUseCase(character.id)
                    _state.update { it.copy(message = SnackbarMessage(R.string.character_deleted, showAction = true)) }
                } else {
                    insertFavoriteCharacterUseCase(character)
                    _state.update { it.copy(message = SnackbarMessage(R.string.character_added)) }
                }
            } catch (error: SQLiteException) {
                _state.update { it.copy(message = SnackbarMessage(R.string.error_message)) }
            }
        }
    }
}