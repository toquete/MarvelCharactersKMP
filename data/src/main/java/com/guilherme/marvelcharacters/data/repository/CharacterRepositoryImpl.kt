package com.guilherme.marvelcharacters.data.repository

import com.guilherme.marvelcharacters.cache.CharacterLocalDataSource
import com.guilherme.marvelcharacters.core.model.Character
import com.guilherme.marvelcharacters.data.infrastructure.annotation.IoDispatcher
import com.guilherme.marvelcharacters.remote.CharacterRemoteDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CharacterRepositoryImpl @Inject constructor(
    private val remoteDataSource: CharacterRemoteDataSource,
    private val localDataSource: CharacterLocalDataSource,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : CharacterRepository {

    override suspend fun getCharacters(name: String, key: String, privateKey: String): List<Character> {
        return withContext(dispatcher) {
            remoteDataSource.getCharacters(name, key, privateKey)
        }
    }

    override fun isCharacterFavorite(id: Int): Flow<Boolean> {
        return localDataSource.isCharacterFavorite(id)
    }

    override fun getFavoriteCharacters(): Flow<List<Character>> {
        return localDataSource.getFavoriteCharacters()
    }

    override suspend fun insertFavoriteCharacter(character: Character) {
        localDataSource.insertFavoriteCharacter(character)
    }

    override suspend fun deleteFavoriteCharacter(character: Character) {
        localDataSource.deleteFavoriteCharacter(character)
    }

    override suspend fun deleteAllFavoriteCharacters() {
        localDataSource.deleteAllFavoriteCharacters()
    }
}