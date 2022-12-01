package com.guilherme.marvelcharacters.cache

import com.guilherme.marvelcharacters.core.model.Character
import kotlinx.coroutines.flow.Flow

interface FavoriteCharacterLocalDataSource {

    fun getFavoriteCharacters(): Flow<List<Character>>

    fun isCharacterFavorite(id: Int): Flow<Boolean>

    suspend fun insertAll(characters: List<Character>)

    suspend fun deleteAll()

    suspend fun delete(character: Character)
}