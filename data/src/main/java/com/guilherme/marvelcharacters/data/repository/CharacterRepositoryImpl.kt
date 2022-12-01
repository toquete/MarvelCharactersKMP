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

    override suspend fun getCharacters(name: String, key: String, privateKey: String): List<Character> {
        return withContext(dispatcher) {
            localDataSource.getCharactersByName(name).ifEmpty {
                remoteDataSource.getCharacters(name, key, privateKey).also {
                    localDataSource.insertAll(it)
                }
            }
        }
    }

    override suspend fun getCharacterById(id: Int, key: String, privateKey: String): Character {
        return withContext(dispatcher) {
            localDataSource.getCharacterById(id) ?: remoteDataSource.getCharacterById(
                id,
                key,
                privateKey
            )
        }
    }

    override fun isCharacterFavorite(id: Int): Flow<Boolean> {
        return favoriteLocalDataSource.isCharacterFavorite(id)
    }

    override fun getFavoriteCharacters(): Flow<List<Character>> {
        return favoriteLocalDataSource.getFavoriteCharacters()
    }

    override suspend fun insertFavoriteCharacter(character: Character) {
        favoriteLocalDataSource.copyFavoriteCharacter(character.id)
    }

    override suspend fun deleteFavoriteCharacter(character: Character) {
        favoriteLocalDataSource.delete(character)
    }

    override suspend fun deleteAllFavoriteCharacters() {
        favoriteLocalDataSource.deleteAll()
    }
}