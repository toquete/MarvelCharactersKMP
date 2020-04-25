package com.guilherme.marvelcharacters.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guilherme.marvelcharacters.data.model.Character
import com.guilherme.marvelcharacters.data.repository.CharacterRepository
import com.guilherme.marvelcharacters.data.source.local.dao.CharacterDao
import kotlinx.coroutines.launch
import java.lang.Exception
import kotlin.coroutines.CoroutineContext

class DetailViewModel(
    private val characterRepository: CharacterRepository,
    private val coroutineContext: CoroutineContext
) : ViewModel() {

    private val _state = MutableLiveData<DetailState>()
    val state: LiveData<DetailState> = _state

    fun onFabClick(character: Character) = viewModelScope.launch(coroutineContext) {
        try {
            characterRepository.insertFavoriteCharacter(character)
            _state.value = DetailState.Success
        } catch (error: Exception) {
            _state.value = DetailState.Error(error)
        }
    }

    sealed class DetailState {
        object Success : DetailState()
        data class Error(val error: Exception) : DetailState()
    }
}