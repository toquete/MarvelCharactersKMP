package com.guilherme.marvelcharacters.feature.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guilherme.marvelcharacters.core.ui.SnackbarManager
import com.guilherme.marvelcharacters.domain.usecase.DeleteAllFavoriteCharactersUseCase
import com.guilherme.marvelcharacters.domain.usecase.GetFavoriteCharactersUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class FavoritesViewModel(
    getFavoriteCharactersUseCase: GetFavoriteCharactersUseCase,
    private val deleteAllFavoriteCharactersUseCase: DeleteAllFavoriteCharactersUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(FavoritesState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            getFavoriteCharactersUseCase()
                .collect { list ->
                    _state.update { it.copy(characters = list) }
                }
        }
    }

    fun onDeleteAllClick() {
        viewModelScope.launch {
            runCatching {
                deleteAllFavoriteCharactersUseCase()
                SnackbarManager.showMessage(R.string.character_deleted)
            }.onFailure {
                SnackbarManager.showMessage(R.string.error_message)
            }
        }
    }
}
