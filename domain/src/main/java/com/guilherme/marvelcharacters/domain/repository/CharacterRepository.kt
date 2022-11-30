package com.guilherme.marvelcharacters.domain.repository

import com.guilherme.marvelcharacters.core.model.Character
import kotlinx.coroutines.flow.Flow

interface CharacterRepository {

    suspend fun getCharacters(
        name: String,
        key: String,
        privateKey: String
    ): List<Character>

    fun isCharacterFavorite(id: Int): Flow<Boolean>

    fun getFavoriteCharacters(): Flow<List<Character>>

    suspend fun insertFavoriteCharacter(character: Character)

    suspend fun deleteFavoriteCharacter(character: Character)

    suspend fun deleteAllFavoriteCharacters()
}