package com.guilherme.marvelcharacters.feature.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guilherme.marvelcharacters.domain.usecase.DeleteAllFavoriteCharactersUseCase
import com.guilherme.marvelcharacters.domain.usecase.GetFavoriteCharactersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class FavoritesViewModel @Inject constructor(
    getFavoriteCharactersUseCase: GetFavoriteCharactersUseCase,
    private val deleteAllFavoriteCharactersUseCase: DeleteAllFavoriteCharactersUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<FavoritesUiState>(FavoritesUiState.Success())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            getFavoriteCharactersUseCase()
                .collect { list ->
                    _uiState.update { FavoritesUiState.Success(list) }
                }
        }
    }

    fun onDeleteAllClick() {
        viewModelScope.launch {
            runCatching {
                deleteAllFavoriteCharactersUseCase()
                _uiState.update { FavoritesUiState.ShowSnackbar(R.string.character_deleted) }
            }.onFailure {
                _uiState.update { FavoritesUiState.ShowSnackbar(R.string.error_message) }
            }
        }
    }

    fun onSnackbarShown() {
        _uiState.update { FavoritesUiState.ShowSnackbar(messageId = null) }
    }
}
