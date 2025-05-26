package com.guilherme.marvelcharacters.feature.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.guilherme.marvelcharacters.domain.usecase.GetFavoriteCharacterByIdUseCase
import com.guilherme.marvelcharacters.domain.usecase.ToggleFavoriteCharacterUseCase
import com.guilherme.marvelcharacters.feature.detail.navigation.DetailRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class DetailViewModel(
    savedStateHandle: SavedStateHandle,
    getFavoriteCharacterByIdUseCase: GetFavoriteCharacterByIdUseCase,
    private val toggleFavoriteCharacterUseCase: ToggleFavoriteCharacterUseCase
) : ViewModel() {

    private val characterId: Int = savedStateHandle.toRoute<DetailRoute>().id

    private val _state = MutableStateFlow(DetailState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            getFavoriteCharacterByIdUseCase(characterId)
                .collect { favoriteCharacter ->
                    _state.update { it.copy(character = favoriteCharacter) }
                }
        }
    }

    fun onFabClick(isFavorite: Boolean) {
        viewModelScope.launch {
            val message = if (isFavorite) R.string.characters_deleted else R.string.character_added

            runCatching {
                toggleFavoriteCharacterUseCase(characterId, isFavorite)
            }.onSuccess {
                _state.update { it.copy(messageId = message, showAction = isFavorite) }
            }.onFailure {
                _state.update { it.copy(messageId = R.string.error_message, showAction = false) }
            }
        }
    }

    fun onUndoClick() {
        viewModelScope.launch {
            runCatching {
                toggleFavoriteCharacterUseCase(characterId, isFavorite = false)
            }.onFailure {
                _state.update { it.copy(messageId = R.string.error_message, showAction = false) }
            }
        }
    }

    fun onSnackbarShown() {
        _state.update { it.copy(messageId = null, showAction = false) }
    }
}