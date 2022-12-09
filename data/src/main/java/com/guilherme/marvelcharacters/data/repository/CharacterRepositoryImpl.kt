package com.guilherme.marvelcharacters.data.repository

import com.guilherme.marvelcharacters.cache.CharacterLocalDataSource
import com.guilherme.marvelcharacters.cache.FavoriteCharacterLocalDataSource
import com.guilherme.marvelcharacters.core.model.Character
import com.guilherme.marvelcharacters.data.infrastructure.annotation.IoDispatcher
import com.guilherme.marvelcharacters.remote.CharacterRemoteDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class CharacterRepositoryImpl @Inject constructor(
    private val remoteDataSource: CharacterRemoteDataSource,
    private val localDataSource: CharacterLocalDataSource,
    private val favoriteLocalDataSource: FavoriteCharacterLocalDataSource,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : CharacterRepository {

    override suspend fun getCharacters(name: String): List<Character> {
        return withContext(dispatcher) {
            localDataSource.getCharactersByName(name).ifEmpty {
                remoteDataSource.getCharacters(name).also {
                    localDataSource.insertAll(it)
                }
            }
        }
    }

    override suspend fun getCharacterById(id: Int): Character {
        return localDataSource.getCharacterById(id)
    }

    override fun isCharacterFavorite(id: Int): Flow<Boolean> {
        return favoriteLocalDataSource.isCharacterFavorite(id)
    }

    override fun getFavoriteCharacters(): Flow<List<Character>> {
        return favoriteLocalDataSource.getFavoriteCharacters()
    }

    override suspend fun insertFavoriteCharacter(id: Int) {
        favoriteLocalDataSource.copyFavoriteCharacter(id)
    }

    override suspend fun deleteFavoriteCharacter(id: Int) {
        favoriteLocalDataSource.delete(id)
    }

    override suspend fun deleteAllFavoriteCharacters() {
        favoriteLocalDataSource.deleteAll()
    }
}