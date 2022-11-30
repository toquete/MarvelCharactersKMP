package com.guilherme.marvelcharacters.cache

import com.guilherme.marvelcharacters.core.model.Character
import kotlinx.coroutines.flow.Flow

interface CharacterLocalDataSource {

    fun isCharacterFavorite(id: Int): Flow<Boolean>

    suspend fun getCharacterById(id: Int): Character?

    fun getFavoriteCharacters(): Flow<List<Character>>

    suspend fun insertFavoriteCharacter(character: Character)

    suspend fun deleteFavoriteCharacter(character: Character)

    suspend fun deleteAllFavoriteCharacters()
}