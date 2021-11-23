package com.guilherme.marvelcharacters.ui.home

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guilherme.marvelcharacters.BuildConfig
import com.guilherme.marvelcharacters.R
import com.guilherme.marvelcharacters.domain.usecase.GetCharactersUseCase
import com.guilherme.marvelcharacters.infrastructure.di.annotation.IoDispatcher
import com.guilherme.marvelcharacters.mapper.CharacterMapper
import com.guilherme.marvelcharacters.model.CharacterVO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getCharactersUseCase: GetCharactersUseCase,
    private val mapper: CharacterMapper = CharacterMapper(),
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _states = MutableStateFlow(State.initialState())
    val states: StateFlow<State> = _states

    private val _events = Channel<Event>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    var query: String? = null

    fun onSearchCharacter(character: String) {
        viewModelScope.launch {
            getCharactersUseCase(character, BuildConfig.MARVEL_KEY, BuildConfig.MARVEL_PRIVATE_KEY)
                .flowOn(dispatcher)
                .onStart { _states.update { it.copy(isLoading = true) } }
                .onCompletion { _states.update { it.copy(isLoading = false) } }
                .catch { error ->
                    // TODO: melhorar tratativa de erro
                    _states.update {
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
                    _states.update { state ->
                        state.copy(
                            characters = list.map { mapper.mapTo(it) },
                            errorMessageId = if (list.isEmpty()) R.string.empty_state_message else null
                        )
                    }
                }
        }
    }

    fun onItemClick(character: CharacterVO) {
        viewModelScope.launch {
            _events.send(Event.NavigateToDetails(character))
        }
    }

    data class State(
        val isLoading: Boolean,
        val characters: List<CharacterVO>,
        @StringRes val errorMessageId: Int?
    ) {

        companion object {
            fun initialState() = State(
                isLoading = false,
                characters = listOf(),
                errorMessageId = null
            )
        }
    }

    sealed class Event {
        data class NavigateToDetails(val character: CharacterVO) : Event()
    }
}