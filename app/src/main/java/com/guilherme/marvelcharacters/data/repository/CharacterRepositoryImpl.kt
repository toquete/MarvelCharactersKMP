package com.guilherme.marvelcharacters.data.repository

import com.guilherme.marvelcharacters.data.mapper.CharacterDataMapper
import com.guilherme.marvelcharacters.data.source.local.CharacterLocalDataSource
import com.guilherme.marvelcharacters.data.source.remote.CharacterRemoteDataSource
import com.guilherme.marvelcharacters.domain.model.Character
import com.guilherme.marvelcharacters.domain.repository.CharacterRepository

class CharacterRepositoryImpl(
    private val remoteDataSource: CharacterRemoteDataSource,
    private val localDataSource: CharacterLocalDataSource,
    private val mapper: CharacterDataMapper = CharacterDataMapper()
) : CharacterRepository {

    override suspend fun getCharacters(name: String): List<Character> {
        return remoteDataSource.getCharacters(name)
            .map { mapper.mapTo(it) }
    }

    override suspend fun isCharacterFavorite(id: Int): Boolean =
        localDataSource.isCharacterFavorite(id)

    override suspend fun getFavoriteCharacters(): List<Character> {
        return localDataSource.getFavoriteCharacters()
            .map { mapper.mapTo(it) }
    }

    override suspend fun insertFavoriteCharacter(character: Character) =
        localDataSource.insertFavoriteCharacter(mapper.mapFrom(character))

    override suspend fun deleteFavoriteCharacter(character: Character) =
        localDataSource.deleteFavoriteCharacter(mapper.mapFrom(character))

    override suspend fun deleteAllFavoriteCharacters() =
        localDataSource.deleteAllFavoriteCharacters()
}