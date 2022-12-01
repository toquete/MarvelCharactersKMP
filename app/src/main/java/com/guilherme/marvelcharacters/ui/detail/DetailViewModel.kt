package com.guilherme.marvelcharacters.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guilherme.marvelcharacters.BuildConfig
import com.guilherme.marvelcharacters.R
import com.guilherme.marvelcharacters.core.model.Character
import com.guilherme.marvelcharacters.domain.usecase.GetFavoriteCharacterByIdUseCase
import com.guilherme.marvelcharacters.domain.usecase.ToggleFavoriteCharacterUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetailViewModel @AssistedInject constructor(
    @Assisted private val character: Character,
    getFavoriteCharacterByIdUseCase: GetFavoriteCharacterByIdUseCase,
    private val toggleFavoriteCharacterUseCase: ToggleFavoriteCharacterUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<DetailUiState>(Loading)
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            getFavoriteCharacterByIdUseCase(
                character.id,
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
                toggleFavoriteCharacterUseCase(character.id, isFavorite)
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
                toggleFavoriteCharacterUseCase(character.id, isFavorite = false)
            }.onFailure {
                _uiState.update { ShowSnackbar(R.string.error_message, showAction = false) }
            }
        }
    }
}