package com.guilherme.marvelcharacters.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guilherme.marvelcharacters.domain.usecase.GetCharactersUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class HomeViewModel(
    private val getCharactersUseCase: GetCharactersUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Empty)
    val uiState = _uiState.asStateFlow()

    var query: String? = null

    fun onSearchCharacter(character: String) {
        _uiState.update { HomeUiState.Loading }

        viewModelScope.launch {
            runCatching {
                getCharactersUseCase(character)
            }.onSuccess { list ->
                _uiState.update {
                    if (list.isEmpty()) {
                        HomeUiState.Error(R.string.empty_state_message)
                    } else {
                        HomeUiState.Success(list)
                    }
                }
            }.onFailure {
                // TODO: melhorar tratativa de erro
                _uiState.update { HomeUiState.Error(R.string.request_error_message) }
            }
        }
    }
}