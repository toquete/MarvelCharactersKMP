package com.guilherme.marvelcharacters.cache

import com.guilherme.marvelcharacters.cache.dao.CharacterDao
import com.guilherme.marvelcharacters.cache.model.CharacterEntity
import com.guilherme.marvelcharacters.cache.model.ImageEntity
import com.guilherme.marvelcharacters.data.model.CharacterData
import com.guilherme.marvelcharacters.data.model.ImageData
import com.guilherme.marvelcharacters.data.source.local.CharacterLocalDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CharacterLocalDataSourceImpl @Inject constructor(
    private val dao: CharacterDao
) : CharacterLocalDataSource {

    override fun isCharacterFavorite(id: Int): Flow<Boolean> = dao.isCharacterFavorite(id)

    override fun getFavoriteCharacters(): Flow<List<CharacterData>> {
        return dao.getCharacterList()
            .map { list ->
                list.map { mapToData(it) }
            }
    }

    override fun getFavoriteCharacter(id: Int): Flow<CharacterData> {
        return dao.getFavoriteCharacter(id)
            .map { mapToData(it) }
    }

    override suspend fun insertFavoriteCharacter(character: CharacterData) = dao.insert(mapToEntity(character))

    override suspend fun deleteFavoriteCharacter(id: Int) = dao.delete(id)

    override suspend fun deleteAllFavoriteCharacters() = dao.deleteAll()

    private fun mapToEntity(origin: CharacterData): CharacterEntity {
        return CharacterEntity(
            id = origin.id,
            name = origin.name,
            description = origin.description,
            thumbnail = ImageEntity(
                path = origin.thumbnail.path,
                extension = origin.thumbnail.extension
            )
        )
    }

    private fun mapToData(origin: CharacterEntity): CharacterData {
        return CharacterData(
            id = origin.id,
            name = origin.name,
            description = origin.description,
            thumbnail = ImageData(
                path = origin.thumbnail.path,
                extension = origin.thumbnail.extension
            ),
            isFavorite = true
        )
    }
}