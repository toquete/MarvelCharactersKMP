package com.guilherme.marvelcharacters.data.repository

import com.guilherme.marvelcharacters.core.model.Character
import kotlinx.coroutines.flow.Flow

interface CharacterRepository {

    suspend fun getCharacters(name: String): List<Character>

    suspend fun getCharacterById(id: Int): Character

    fun isCharacterFavorite(id: Int): Flow<Boolean>

    fun getFavoriteCharacters(): Flow<List<Character>>

    suspend fun insertFavoriteCharacter(id: Int)

    suspend fun deleteFavoriteCharacter(id: Int)

    suspend fun deleteAllFavoriteCharacters()
}