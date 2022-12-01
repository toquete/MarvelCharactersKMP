package com.guilherme.marvelcharacters.ui.detail

import android.database.sqlite.SQLiteException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guilherme.marvelcharacters.BuildConfig
import com.guilherme.marvelcharacters.R
import com.guilherme.marvelcharacters.core.model.Character
import com.guilherme.marvelcharacters.domain.model.FavoriteCharacter
import com.guilherme.marvelcharacters.domain.usecase.DeleteFavoriteCharacterUseCase
import com.guilherme.marvelcharacters.domain.usecase.GetFavoriteCharacterByIdUseCase
import com.guilherme.marvelcharacters.domain.usecase.InsertFavoriteCharacterUseCase
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
    private val deleteFavoriteCharacterUseCase: DeleteFavoriteCharacterUseCase,
    private val insertFavoriteCharacterUseCase: InsertFavoriteCharacterUseCase
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
            try {
                if (character.isFavorite) {
                    deleteFavoriteCharacterUseCase(character.character)
                    _uiState.update { ShowSnackbar(R.string.character_deleted, showAction = true) }
                } else {
                    insertFavoriteCharacterUseCase(character.character)
                    _uiState.update { ShowSnackbar(R.string.character_added, showAction = false) }
                }
            } catch (error: SQLiteException) {
                _uiState.update { ShowSnackbar(R.string.error_message, showAction = false) }
            }
        }
    }

    fun onUndoClick() {
        viewModelScope.launch {
            try {
                insertFavoriteCharacterUseCase(character)
            } catch (error: SQLiteException) {
                _uiState.update { ShowSnackbar(R.string.error_message, showAction = false) }
            }
        }
    }
}