package com.guilherme.marvelcharacters.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guilherme.marvelcharacters.data.model.Character
import com.guilherme.marvelcharacters.data.repository.CharacterRepository
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class DetailViewModel(
    private val character: Character,
    private val characterRepository: CharacterRepository,
    private val coroutineContext: CoroutineContext
) : ViewModel() {

    private val _state = MutableLiveData<DetailState>()
    val state: LiveData<DetailState> = _state

    val isCharacterFavorite: LiveData<Boolean> = characterRepository.isCharacterFavorite(character.id)

    fun onFabClick() = viewModelScope.launch(coroutineContext) {
        try {
            isCharacterFavorite.value?.let { isFavorite ->
                if (isFavorite) {
                    characterRepository.deleteFavoriteCharacter(character)
                    _state.value = DetailState.CharacterDeleted
                } else {
                    characterRepository.insertFavoriteCharacter(character)
                    _state.value = DetailState.CharacterSaved
                }
            } ?: run {
                _state.value = DetailState.Error(Exception("Does this character exist??"))
            }
        } catch (error: Exception) {
            _state.value = DetailState.Error(error)
        }
    }

    fun onUndoClick() = viewModelScope.launch(coroutineContext) {
        try {
            characterRepository.insertFavoriteCharacter(character)
        } catch (error: Exception) {
            _state.value = DetailState.Error(error)
        }
    }

    sealed class DetailState {
        object CharacterSaved : DetailState()
        object CharacterDeleted : DetailState()
        data class Error(val error: Exception) : DetailState()
    }
}