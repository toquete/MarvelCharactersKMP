package com.guilherme.marvelcharacters.feature.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guilherme.marvelcharacters.domain.usecase.GetFavoriteCharacterByIdUseCase
import com.guilherme.marvelcharacters.domain.usecase.ToggleFavoriteCharacterUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class DetailViewModel(
    savedStateHandle: SavedStateHandle,
    getFavoriteCharacterByIdUseCase: GetFavoriteCharacterByIdUseCase,
    private val toggleFavoriteCharacterUseCase: ToggleFavoriteCharacterUseCase
) : ViewModel() {

    private val characterId: Int = checkNotNull(savedStateHandle["characterId"])

    private val _uiState = MutableStateFlow<DetailUiState>(DetailUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            getFavoriteCharacterByIdUseCase(characterId)
                .collect { favoriteCharacter ->
                    _uiState.update { DetailUiState.Success(favoriteCharacter) }
                }
        }
    }

    fun onFabClick(isFavorite: Boolean) {
        viewModelScope.launch {
            val message = if (isFavorite) R.string.character_deleted else R.string.character_added

            runCatching {
                toggleFavoriteCharacterUseCase(characterId, isFavorite)
            }.onSuccess {
                _uiState.update { DetailUiState.ShowSnackbar(message, showAction = isFavorite) }
            }.onFailure {
                _uiState.update { DetailUiState.ShowSnackbar(R.string.error_message, showAction = false) }
            }
        }
    }

    fun onUndoClick() {
        viewModelScope.launch {
            runCatching {
                toggleFavoriteCharacterUseCase(characterId, isFavorite = false)
            }.onFailure {
                _uiState.update { DetailUiState.ShowSnackbar(R.string.error_message, showAction = false) }
            }
        }
    }

    fun onSnackbarShown() {
        _uiState.update { DetailUiState.ShowSnackbar(messageId = null, showAction = false) }
    }
}