package com.guilherme.marvelcharacters.ui.detail

import android.database.sqlite.SQLiteException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guilherme.marvelcharacters.BuildConfig
import com.guilherme.marvelcharacters.R
import com.guilherme.marvelcharacters.core.model.Character
import com.guilherme.marvelcharacters.domain.model.FavoriteCharacter
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

    fun onFabClick(character: FavoriteCharacter) {
        viewModelScope.launch {
            val message = if (character.isFavorite) R.string.character_deleted else R.string.character_added

            runCatching {
                toggleFavoriteCharacterUseCase(character.character.id, character.isFavorite)
            }.onSuccess {
                _uiState.update { ShowSnackbar(message, showAction = character.isFavorite) }
            }.onFailure {
                _uiState.update { ShowSnackbar(R.string.error_message, showAction = false) }
            }
        }
    }

    fun onUndoClick() {
        viewModelScope.launch {
            try {
                toggleFavoriteCharacterUseCase(character.id, isFavorite = false)
            } catch (error: SQLiteException) {
                _uiState.update { ShowSnackbar(R.string.error_message, showAction = false) }
            }
        }
    }
}