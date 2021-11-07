package com.guilherme.marvelcharacters.domain.repository

import com.guilherme.marvelcharacters.domain.model.Character
import kotlinx.coroutines.flow.Flow

interface CharacterRepository {

    fun getCharacters(name: String): Flow<List<Character>>

    fun isCharacterFavorite(id: Int): Flow<Boolean>

    fun getFavoriteCharacters(): Flow<List<Character>>

    suspend fun insertFavoriteCharacter(character: Character)

    suspend fun deleteFavoriteCharacter(character: Character)

    suspend fun deleteAllFavoriteCharacters()
}