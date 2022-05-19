package com.guilherme.marvelcharacters.data.source.local

import com.guilherme.marvelcharacters.data.model.CharacterData
import kotlinx.coroutines.flow.Flow

interface CharacterLocalDataSource {

    fun isCharacterFavorite(id: Int): Flow<Boolean>

    fun getFavoriteCharacters(): Flow<List<CharacterData>>

    fun getFavoriteCharacter(id: Int): Flow<CharacterData>

    suspend fun insertFavoriteCharacter(character: CharacterData)

    suspend fun deleteFavoriteCharacter(id: Int)

    suspend fun deleteAllFavoriteCharacters()
}