package com.guilherme.marvelcharacters.ui.home

import androidx.lifecycle.viewModelScope
import com.guilherme.marvelcharacters.BuildConfig
import com.guilherme.marvelcharacters.R
import com.guilherme.marvelcharacters.domain.usecase.GetCharactersUseCase
import com.guilherme.marvelcharacters.infrastructure.BaseViewModel
import com.guilherme.marvelcharacters.mapper.CharacterMapper
import com.guilherme.marvelcharacters.model.CharacterVO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getCharactersUseCase: GetCharactersUseCase,
    private val mapper: CharacterMapper
) : BaseViewModel<HomeState, HomeEvent>(HomeState.initialState()) {

    var query: String? = null

    fun onSearchCharacter(character: String) {
        viewModelScope.launch {
            setState { it.showLoading() }
            runCatching {
                getCharactersUseCase(character, BuildConfig.MARVEL_KEY, BuildConfig.MARVEL_PRIVATE_KEY)
            }.onSuccess { list ->
                setState { state ->
                    state.copy(
                        isLoading = false,
                        characters = list.map { mapper.mapTo(it) },
                        errorMessageId = if (list.isEmpty()) R.string.empty_state_message else null
                    )
                }
            }.onFailure { error ->
                // TODO: melhorar tratativa de erro
                setState {
                    it.copy(
                        isLoading = false,
                        errorMessageId = R.string.request_error_message
                    )
                }
            }
        }
    }

    fun onItemClick(character: CharacterVO) {
        sendEvent(HomeEvent.NavigateToDetails(character))
    }
}