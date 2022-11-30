package com.guilherme.marvelcharacters.cache

import com.guilherme.marvelcharacters.cache.dao.CharacterDao
import com.guilherme.marvelcharacters.cache.model.CharacterEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CharacterLocalDataSourceImpl @Inject constructor(
    private val dao: CharacterDao
) : CharacterLocalDataSource {

    override fun isCharacterFavorite(id: Int): Flow<Boolean> = dao.isCharacterFavorite(id)

    override fun getFavoriteCharacters(): Flow<List<CharacterEntity>> {
        return dao.getCharacterList()
    }

    override suspend fun insertFavoriteCharacter(character: CharacterEntity) {
        dao.insert(character)
    }

    override suspend fun deleteFavoriteCharacter(character: CharacterEntity) {
        dao.delete(character)
    }

    override suspend fun deleteAllFavoriteCharacters() = dao.deleteAll()
}