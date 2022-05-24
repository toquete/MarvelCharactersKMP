package com.guilherme.marvelcharacters.data.repository

import com.guilherme.marvelcharacters.data.model.CharacterData
import com.guilherme.marvelcharacters.data.source.local.CharacterLocalDataSource
import com.guilherme.marvelcharacters.data.source.remote.CharacterRemoteDataSource
import com.guilherme.marvelcharacters.domain.model.Character
import com.guilherme.marvelcharacters.domain.repository.CharacterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class CharacterRepositoryImpl @Inject constructor(
    private val remoteDataSource: CharacterRemoteDataSource,
    private val localDataSource: CharacterLocalDataSource
) : CharacterRepository {

    override fun getCharacters(name: String, key: String, privateKey: String): Flow<List<Character>> {
        return remoteDataSource.getCharacters(name, key, privateKey)
            .onEach { localDataSource.saveInCache(it) }
            .map { list ->
                list.map { mapToDomain(it) }
            }
    }

    override fun isCharacterFavorite(id: Int): Flow<Boolean> {
        return localDataSource.isCharacterFavorite(id)
    }

    override fun getFavoriteCharacters(): Flow<List<Character>> {
        return localDataSource.getFavoriteCharacters()
            .map { list ->
                list.map { mapToDomain(it) }
            }
    }

    override fun getCharacter(id: Int): Flow<Character> {
        return localDataSource.getCharacter(id)
            .map { mapToDomain(it) }
    }

    override suspend fun insertFavoriteCharacter(character: Character) {
        localDataSource.insertFavoriteCharacter(mapToData(character))
    }

    override suspend fun deleteFavoriteCharacter(id: Int) {
        localDataSource.deleteFavoriteCharacter(id)
    }

    override suspend fun deleteAllFavoriteCharacters() {
        localDataSource.deleteAllFavoriteCharacters()
    }

    private fun mapToDomain(origin: CharacterData): Character {
        return Character(
            id = origin.id,
            name = origin.name,
            description = origin.description,
            thumbnail = origin.thumbnail
        )
    }

    private fun mapToData(origin: Character): CharacterData {
        return CharacterData(
            id = origin.id,
            name = origin.name,
            description = origin.description,
            thumbnail = origin.thumbnail
        )
    }
}