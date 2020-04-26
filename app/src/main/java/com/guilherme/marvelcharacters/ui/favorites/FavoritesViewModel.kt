package com.guilherme.marvelcharacters.ui.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guilherme.marvelcharacters.data.model.Character
import com.guilherme.marvelcharacters.data.repository.CharacterRepository
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class FavoritesViewModel(
    private val characterRepository: CharacterRepository,
    private val coroutineContext: CoroutineContext
) : ViewModel() {

    val list: LiveData<List<Character>> = characterRepository.getFavoriteCharacters()

    fun deleteCharacter(character: Character) = viewModelScope.launch(coroutineContext) {
        characterRepository.deleteFavoriteCharacter(character)
    }

    fun deleteAllCharacters() = viewModelScope.launch(coroutineContext) { characterRepository.deleteAllFavoriteCharacters() }
}
