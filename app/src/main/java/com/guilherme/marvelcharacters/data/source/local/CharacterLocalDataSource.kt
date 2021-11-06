package com.guilherme.marvelcharacters.data.source.local

import com.guilherme.marvelcharacters.data.model.CharacterData

interface CharacterLocalDataSource {

    suspend fun isCharacterFavorite(id: Int): Boolean

    suspend fun getFavoriteCharacters(): List<CharacterData>

    suspend fun insertFavoriteCharacter(character: CharacterData)

    suspend fun deleteFavoriteCharacter(character: CharacterData)

    suspend fun deleteAllFavoriteCharacters()
}