package com.guilherme.marvelcharacters.feature.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guilherme.marvelcharacters.core.ui.SnackbarMessage
import com.guilherme.marvelcharacters.core.ui.UiText
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
                _state.update {
                    it.copy(
                        snackbarMessage = SnackbarMessage(
                            text = UiText.ResourceString(R.string.character_deleted)
                        )
                    )
                }
            }.onFailure {
                _state.update {
                    it.copy(
                        snackbarMessage = SnackbarMessage(
                            text = UiText.ResourceString(R.string.error_message)
                        )
                    )
                }
            }
        }
    }

    fun onSnackbarShown() {
        _state.update { it.copy(snackbarMessage = null) }
    }
}
