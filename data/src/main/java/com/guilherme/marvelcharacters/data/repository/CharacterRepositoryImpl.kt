package com.guilherme.marvelcharacters.data.repository

import com.guilherme.marvelcharacters.cache.CharacterLocalDataSource
import com.guilherme.marvelcharacters.cache.model.toExternalModel
import com.guilherme.marvelcharacters.core.model.Character
import com.guilherme.marvelcharacters.core.model.Image
import com.guilherme.marvelcharacters.data.annotation.IoDispatcher
import com.guilherme.marvelcharacters.data.extension.toEntity
import com.guilherme.marvelcharacters.data.model.CharacterData
import com.guilherme.marvelcharacters.data.source.remote.CharacterRemoteDataSource
import com.guilherme.marvelcharacters.domain.repository.CharacterRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
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
                .map { data ->
                    mapToDomain(data)
                }
        }
    }

    override fun isCharacterFavorite(id: Int): Flow<Boolean> {
        return localDataSource.isCharacterFavorite(id)
    }

    override fun getFavoriteCharacters(): Flow<List<Character>> {
        return localDataSource.getFavoriteCharacters()
            .map { list ->
                list.map { it.toExternalModel() }
            }
    }

    override suspend fun insertFavoriteCharacter(character: Character) {
        localDataSource.insertFavoriteCharacter(character.toEntity())
    }

    override suspend fun deleteFavoriteCharacter(character: Character) {
        localDataSource.deleteFavoriteCharacter(character.toEntity())
    }

    override suspend fun deleteAllFavoriteCharacters() {
        localDataSource.deleteAllFavoriteCharacters()
    }

    private fun mapToDomain(origin: CharacterData): Character {
        return Character(
            id = origin.id,
            name = origin.name,
            description = origin.description,
            thumbnail = Image(
                path = origin.thumbnail.path,
                extension = origin.thumbnail.extension
            )
        )
    }
}