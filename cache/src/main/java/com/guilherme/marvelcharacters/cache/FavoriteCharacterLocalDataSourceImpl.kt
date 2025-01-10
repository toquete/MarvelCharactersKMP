package com.guilherme.marvelcharacters.cache

import com.guilherme.marvelcharacters.cache.dao.FavoriteCharacterDao
import com.guilherme.marvelcharacters.cache.model.FavoriteCharacterEntity
import com.guilherme.marvelcharacters.cache.model.toExternalModel
import com.guilherme.marvelcharacters.core.model.Character
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class FavoriteCharacterLocalDataSourceImpl(
    private val dao: FavoriteCharacterDao
) : FavoriteCharacterLocalDataSource {

    override fun getFavoriteCharacters(): Flow<List<Character>> {
        return dao.getFavoriteCharacters().map {
            it.map(FavoriteCharacterEntity::toExternalModel)
        }
    }

    override fun isCharacterFavorite(id: Int): Flow<Boolean> {
        return dao.isCharacterFavorite(id)
    }

    override suspend fun copyFavoriteCharacter(id: Int) {
        dao.copyFavoriteCharacter(id)
    }

    override suspend fun deleteAll() {
        dao.deleteAll()
    }

    override suspend fun delete(id: Int) {
        dao.delete(id)
    }
}