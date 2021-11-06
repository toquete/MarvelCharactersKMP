package com.guilherme.marvelcharacters.data.source.local

import com.guilherme.marvelcharacters.data.model.CharacterData
import com.guilherme.marvelcharacters.data.source.local.dao.CharacterDao
import com.guilherme.marvelcharacters.data.source.local.mapper.CharacterEntityMapper

class CharacterLocalDataSourceImpl(
    private val dao: CharacterDao,
    private val mapper: CharacterEntityMapper = CharacterEntityMapper()
) : CharacterLocalDataSource {

    override suspend fun isCharacterFavorite(id: Int): Boolean = dao.isCharacterFavorite(id)

    override suspend fun getFavoriteCharacters(): List<CharacterData> {
        return dao.getCharacterList()
            .map { mapper.mapTo(it) }
    }

    override suspend fun insertFavoriteCharacter(character: CharacterData) =
        dao.insert(mapper.mapFrom(character))

    override suspend fun deleteFavoriteCharacter(character: CharacterData) =
        dao.delete(mapper.mapFrom(character))

    override suspend fun deleteAllFavoriteCharacters() = dao.deleteAll()
}