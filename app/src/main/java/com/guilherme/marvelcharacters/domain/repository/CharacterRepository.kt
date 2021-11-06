package com.guilherme.marvelcharacters.domain.repository

import com.guilherme.marvelcharacters.domain.model.Character

interface CharacterRepository {

    suspend fun getCharacters(name: String): List<Character>

    suspend fun isCharacterFavorite(id: Int): Boolean

    suspend fun getFavoriteCharacters(): List<Character>

    suspend fun insertFavoriteCharacter(character: Character)

    suspend fun deleteFavoriteCharacter(character: Character)

    suspend fun deleteAllFavoriteCharacters()
}