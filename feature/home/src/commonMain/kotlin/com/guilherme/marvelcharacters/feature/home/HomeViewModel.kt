package com.guilherme.marvelcharacters.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guilherme.marvelcharacters.core.ui.Resources
import com.guilherme.marvelcharacters.domain.usecase.GetCharactersUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class HomeViewModel(
    private val getCharactersUseCase: GetCharactersUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    fun onSearchCharacter(character: String) {
        _state.update {
            it.copy(
                characters = emptyList(),
                errorMessageId = null,
                isLoading = true
            )
        }

        viewModelScope.launch {
            runCatching {
                getCharactersUseCase(character)
            }.onSuccess { list ->
                _state.update {
                    if (list.isEmpty()) {
                        it.copy(
                            errorMessageId = Resources.String.EmptyStateMessage,
                            isLoading = false
                        )
                    } else {
                        it.copy(
                            characters = list,
                            errorMessageId = null,
                            isLoading = false
                        )
                    }
                }
            }.onFailure {
                // TODO: melhorar tratativa de erro
                _state.update {
                    it.copy(
                        errorMessageId = Resources.String.RequestErrorMessage,
                        isLoading = false
                    )
                }
            }
        }
    }
}