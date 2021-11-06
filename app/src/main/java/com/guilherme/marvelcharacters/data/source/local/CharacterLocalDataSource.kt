package com.guilherme.marvelcharacters.data.source.local

import com.guilherme.marvelcharacters.domain.model.Character

interface CharacterLocalDataSource {

    suspend fun isCharacterFavorite(id: Int): Boolean

    suspend fun getFavoriteCharacters(): List<Character>

    suspend fun insertFavoriteCharacter(character: Character)

    suspend fun deleteFavoriteCharacter(character: Character)

    suspend fun deleteAllFavoriteCharacters()
}