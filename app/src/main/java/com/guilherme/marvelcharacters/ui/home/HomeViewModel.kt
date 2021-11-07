package com.guilherme.marvelcharacters.ui.home

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guilherme.marvelcharacters.Event
import com.guilherme.marvelcharacters.R
import com.guilherme.marvelcharacters.data.repository.PreferenceRepository
import com.guilherme.marvelcharacters.domain.usecase.GetCharactersUseCase
import com.guilherme.marvelcharacters.ui.mapper.CharacterMapper
import com.guilherme.marvelcharacters.ui.model.CharacterVO
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import retrofit2.HttpException

class HomeViewModel(
    private val preferenceRepository: PreferenceRepository,
    private val getCharactersUseCase: GetCharactersUseCase,
    private val mapper: CharacterMapper = CharacterMapper(),
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    private val _states = MutableLiveData<CharacterListState>()
    val states: LiveData<CharacterListState> = _states

    private val _navigateToDetail = MutableLiveData<Event<CharacterVO>>()
    val navigateToDetail: LiveData<Event<CharacterVO>> = _navigateToDetail

    val nightMode: LiveData<Int> = preferenceRepository.nightModeLive

    var query: String? = null

    fun onSearchCharacter(character: String) {
        viewModelScope.launch {
            getCharactersUseCase(character)
                .flowOn(dispatcher)
                .onStart { _states.value = CharacterListState.Loading }
                .catch { error ->
                    // TODO: melhorar tratativa de erro
                    _states.value = if (error is HttpException) {
                        CharacterListState.ErrorState(R.string.request_error_message)
                    } else {
                        CharacterListState.ErrorState(R.string.network_error_message)
                    }
                }
                .collect { list ->
                    _states.value = if (list.isEmpty()) {
                        CharacterListState.EmptyState
                    } else {
                        val mappedList = list.map { mapper.mapTo(it) }
                        CharacterListState.Characters(mappedList)
                    }
                }
        }
    }

    fun onItemClick(character: CharacterVO) {
        _navigateToDetail.value = Event(character)
    }

    fun onActionItemClick() {
        preferenceRepository.isDarkTheme = !preferenceRepository.isDarkTheme
    }

    sealed class CharacterListState {
        data class Characters(val characters: List<CharacterVO>) : CharacterListState()
        data class ErrorState(@StringRes val messageId: Int) : CharacterListState()
        object EmptyState : CharacterListState()
        object Loading : CharacterListState()
    }
}