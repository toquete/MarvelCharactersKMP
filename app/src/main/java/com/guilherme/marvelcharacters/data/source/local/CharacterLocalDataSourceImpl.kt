package com.guilherme.marvelcharacters.data.source.local

import com.guilherme.marvelcharacters.data.model.CharacterEntity
import com.guilherme.marvelcharacters.data.source.local.dao.CharacterDao
import com.guilherme.marvelcharacters.data.source.local.mapper.CharacterEntityMapper
import com.guilherme.marvelcharacters.domain.model.Character
import com.guilherme.marvelcharacters.infrastructure.util.Mapper

class CharacterLocalDataSourceImpl(
    private val dao: CharacterDao,
    private val mapper: Mapper<CharacterEntity, Character> = CharacterEntityMapper()
) : CharacterLocalDataSource {

    override suspend fun isCharacterFavorite(id: Int): Boolean = dao.isCharacterFavorite(id)

    override suspend fun getFavoriteCharacters(): List<Character> {
        return dao.getCharacterList()
            .map { mapper.mapTo(it) }
    }

    override suspend fun insertFavoriteCharacter(character: Character) = dao.insert(mapper.mapFrom(character))

    override suspend fun deleteFavoriteCharacter(character: Character) = dao.delete(mapper.mapFrom(character))

    override suspend fun deleteAllFavoriteCharacters() = dao.deleteAll()
}