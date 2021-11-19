package com.guilherme.marvelcharacters.cache

import com.guilherme.marvelcharacters.cache.dao.CharacterDao
import com.guilherme.marvelcharacters.cache.mapper.CharacterEntityMapper
import com.guilherme.marvelcharacters.data.model.CharacterData
import com.guilherme.marvelcharacters.data.source.local.CharacterLocalDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CharacterLocalDataSourceImpl @Inject constructor(
    private val dao: CharacterDao,
    private val mapper: CharacterEntityMapper
) : CharacterLocalDataSource {

    override fun isCharacterFavorite(id: Int): Flow<Boolean> = dao.isCharacterFavorite(id)

    override fun getFavoriteCharacters(): Flow<List<CharacterData>> {
        return dao.getCharacterList()
            .map { list ->
                list.map { mapper.mapTo(it) }
            }
    }

    override suspend fun insertFavoriteCharacter(character: CharacterData) =
        dao.insert(mapper.mapFrom(character))

    override suspend fun deleteFavoriteCharacter(character: CharacterData) =
        dao.delete(mapper.mapFrom(character))

    override suspend fun deleteAllFavoriteCharacters() = dao.deleteAll()
}