package com.guilherme.marvelcharacters.data.repository

import com.guilherme.marvelcharacters.data.annotation.IoDispatcher
import com.guilherme.marvelcharacters.data.model.CharacterData
import com.guilherme.marvelcharacters.data.model.ImageData
import com.guilherme.marvelcharacters.data.source.local.CharacterLocalDataSource
import com.guilherme.marvelcharacters.data.source.remote.CharacterRemoteDataSource
import com.guilherme.marvelcharacters.domain.model.Character
import com.guilherme.marvelcharacters.domain.model.Image
import com.guilherme.marvelcharacters.domain.repository.CharacterRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CharacterRepositoryImpl @Inject constructor(
    private val remoteDataSource: CharacterRemoteDataSource,
    private val localDataSource: CharacterLocalDataSource,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : CharacterRepository {

    override fun getCharacters(name: String, key: String, privateKey: String): Flow<List<Character>> {
        return remoteDataSource.getCharacters(name, key, privateKey)
            .flowOn(dispatcher)
            .map { list ->
                list.map { mapToDomain(it) }
            }
    }

    override fun isCharacterFavorite(id: Int): Flow<Boolean> {
        return localDataSource.isCharacterFavorite(id)
            .flowOn(dispatcher)
    }

    override fun getFavoriteCharacters(): Flow<List<Character>> {
        return localDataSource.getFavoriteCharacters()
            .flowOn(dispatcher)
            .map { list ->
                list.map { mapToDomain(it) }
            }
    }

    override suspend fun insertFavoriteCharacter(character: Character) {
        withContext(dispatcher) {
            localDataSource.insertFavoriteCharacter(mapToData(character))
        }
    }

    override suspend fun deleteFavoriteCharacter(character: Character) {
        withContext(dispatcher) {
            localDataSource.deleteFavoriteCharacter(mapToData(character))
        }
    }

    override suspend fun deleteAllFavoriteCharacters() {
        withContext(dispatcher) {
            localDataSource.deleteAllFavoriteCharacters()
        }
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

    private fun mapToData(origin: Character): CharacterData {
        return CharacterData(
            id = origin.id,
            name = origin.name,
            description = origin.description,
            thumbnail = ImageData(
                path = origin.thumbnail.path,
                extension = origin.thumbnail.extension
            )
        )
    }
}