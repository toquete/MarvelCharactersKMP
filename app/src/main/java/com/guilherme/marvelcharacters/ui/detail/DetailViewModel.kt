package com.guilherme.marvelcharacters.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guilherme.marvelcharacters.BuildConfig
import com.guilherme.marvelcharacters.R
import com.guilherme.marvelcharacters.domain.usecase.GetFavoriteCharacterByIdUseCase
import com.guilherme.marvelcharacters.domain.usecase.ToggleFavoriteCharacterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getFavoriteCharacterByIdUseCase: GetFavoriteCharacterByIdUseCase,
    private val toggleFavoriteCharacterUseCase: ToggleFavoriteCharacterUseCase
) : ViewModel() {

    private val characterId: Int = checkNotNull(savedStateHandle["characterId"])

    private val _uiState = MutableStateFlow<DetailUiState>(Loading)
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            getFavoriteCharacterByIdUseCase(
                characterId,
                BuildConfig.MARVEL_KEY,
                BuildConfig.MARVEL_PRIVATE_KEY
            ).collect { favoriteCharacter ->
                _uiState.update { Success(favoriteCharacter) }
            }
        }
    }

    fun onFabClick(isFavorite: Boolean) {
        viewModelScope.launch {
            val message = if (isFavorite) R.string.character_deleted else R.string.character_added

            runCatching {
                toggleFavoriteCharacterUseCase(characterId, isFavorite)
            }.onSuccess {
                _uiState.update { ShowSnackbar(message, showAction = isFavorite) }
            }.onFailure {
                _uiState.update { ShowSnackbar(R.string.error_message, showAction = false) }
            }
        }
    }

    fun onUndoClick() {
        viewModelScope.launch {
            runCatching {
                toggleFavoriteCharacterUseCase(characterId, isFavorite = false)
            }.onFailure {
                _uiState.update { ShowSnackbar(R.string.error_message, showAction = false) }
            }
        }
    }
}