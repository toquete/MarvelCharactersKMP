package com.guilherme.marvelcharacters.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guilherme.marvelcharacters.BuildConfig
import com.guilherme.marvelcharacters.R
import com.guilherme.marvelcharacters.domain.usecase.GetCharactersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getCharactersUseCase: GetCharactersUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    fun onSearchCharacter(character: String) {
        viewModelScope.launch {
            getCharactersUseCase(character, BuildConfig.MARVEL_KEY, BuildConfig.MARVEL_PRIVATE_KEY)
                .onStart { _state.update { it.copy(isLoading = true) } }
                .onCompletion { _state.update { it.copy(isLoading = false) } }
                .catch { error ->
                    // TODO: melhorar tratativa de erro
                    _state.update {
                        it.copy(
                            errorMessageId = if (error is HttpException) {
                                R.string.request_error_message
                            } else {
                                R.string.network_error_message
                            }
                        )
                    }
                }
                .collect { list ->
                    _state.update { state ->
                        state.copy(
                            characters = list,
                            errorMessageId = if (list.isEmpty()) R.string.empty_state_message else null
                        )
                    }
                }
        }
    }
}