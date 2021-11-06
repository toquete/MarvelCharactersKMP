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
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class HomeViewModel(
    private val preferenceRepository: PreferenceRepository,
    private val getCharactersUseCase: GetCharactersUseCase,
    private val mapper: CharacterMapper = CharacterMapper()
) : ViewModel() {

    private val _states = MutableLiveData<CharacterListState>()
    val states: LiveData<CharacterListState> = _states

    private val _navigateToDetail = MutableLiveData<Event<CharacterVO>>()
    val navigateToDetail: LiveData<Event<CharacterVO>> = _navigateToDetail

    val nightMode: LiveData<Int> = preferenceRepository.nightModeLive

    var query: String? = null

    fun onSearchCharacter(character: String) {
        viewModelScope.launch {
            _states.value = CharacterListState.Loading
            try {
                val charactersList = getCharactersUseCase(character)
                    .map { mapper.mapTo(it) }

                _states.value = if (charactersList.isEmpty()) {
                    CharacterListState.EmptyState
                } else {
                    CharacterListState.Characters(charactersList)
                }
            } catch (error: HttpException) {
                _states.value = CharacterListState.ErrorState(R.string.request_error_message)
            } catch (error: IOException) {
                _states.value = CharacterListState.ErrorState(R.string.network_error_message)
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