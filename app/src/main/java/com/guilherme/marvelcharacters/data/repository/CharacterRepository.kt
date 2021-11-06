package com.guilherme.marvelcharacters.data.repository

import com.guilherme.marvelcharacters.data.source.local.CharacterLocalDataSource
import com.guilherme.marvelcharacters.data.source.remote.CharacterRemoteDataSource
import com.guilherme.marvelcharacters.domain.model.Character

class CharacterRepository(
    private val remoteDataSource: CharacterRemoteDataSource,
    private val localDataSource: CharacterLocalDataSource
) {

    suspend fun getCharacters(name: String): List<Character> {
        return remoteDataSource.getCharacters(name)
    }

    suspend fun isCharacterFavorite(id: Int): Boolean = localDataSource.isCharacterFavorite(id)

    suspend fun getFavoriteCharacters(): List<Character> = localDataSource.getFavoriteCharacters()

    suspend fun insertFavoriteCharacter(character: Character) = localDataSource.insertFavoriteCharacter(character)

    suspend fun deleteFavoriteCharacter(character: Character) = localDataSource.deleteFavoriteCharacter(character)

    suspend fun deleteAllFavoriteCharacters() = localDataSource.deleteAllFavoriteCharacters()
}