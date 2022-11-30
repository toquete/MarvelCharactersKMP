package com.guilherme.marvelcharacters.cache

import com.guilherme.marvelcharacters.cache.model.CharacterEntity
import kotlinx.coroutines.flow.Flow

interface CharacterLocalDataSource {

    fun isCharacterFavorite(id: Int): Flow<Boolean>

    fun getFavoriteCharacters(): Flow<List<CharacterEntity>>

    suspend fun insertFavoriteCharacter(character: CharacterEntity)

    suspend fun deleteFavoriteCharacter(character: CharacterEntity)

    suspend fun deleteAllFavoriteCharacters()
}