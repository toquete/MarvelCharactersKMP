package com.guilherme.marvelcharacters.ui.home

import androidx.lifecycle.viewModelScope
import com.guilherme.marvelcharacters.BuildConfig
import com.guilherme.marvelcharacters.R
import com.guilherme.marvelcharacters.domain.usecase.GetCharactersUseCase
import com.guilherme.marvelcharacters.infrastructure.BaseViewModel
import com.guilherme.marvelcharacters.mapper.CharacterMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getCharactersUseCase: GetCharactersUseCase,
    private val mapper: CharacterMapper
) : BaseViewModel<HomeState, HomeEvent>(HomeState.initialState()) {

    fun onSearchCharacter(character: String) {
        viewModelScope.launch {
            getCharactersUseCase(character, BuildConfig.MARVEL_KEY, BuildConfig.MARVEL_PRIVATE_KEY)
                .onStart { setState { it.showLoading() } }
                .onCompletion { setState { it.hideLoading() } }
                .catch { error ->
                    // TODO: melhorar tratativa de erro
                    setState {
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
                    setState { state ->
                        state.copy(
                            characters = list.map { mapper.mapTo(it) },
                            errorMessageId = if (list.isEmpty()) R.string.empty_state_message else null
                        )
                    }
                }
        }
    }
}