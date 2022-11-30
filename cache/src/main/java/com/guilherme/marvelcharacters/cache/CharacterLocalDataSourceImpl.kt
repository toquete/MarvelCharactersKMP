package com.guilherme.marvelcharacters.cache

import com.guilherme.marvelcharacters.cache.dao.CharacterDao
import com.guilherme.marvelcharacters.cache.extension.toEntity
import com.guilherme.marvelcharacters.cache.model.CharacterEntity
import com.guilherme.marvelcharacters.cache.model.toExternalModel
import com.guilherme.marvelcharacters.core.model.Character
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class CharacterLocalDataSourceImpl @Inject constructor(
    private val dao: CharacterDao
) : CharacterLocalDataSource {

    override fun isCharacterFavorite(id: Int): Flow<Boolean> = dao.isCharacterFavorite(id)

    override suspend fun getCharacterById(id: Int): Character? {
        return dao.getCharacterById(id)?.toExternalModel()
    }

    override fun getFavoriteCharacters(): Flow<List<Character>> {
        return dao.getCharacterList().map {
            it.map(CharacterEntity::toExternalModel)
        }
    }

    override suspend fun insertFavoriteCharacter(character: Character) {
        dao.insert(character.toEntity())
    }

    override suspend fun deleteFavoriteCharacter(character: Character) {
        dao.delete(character.toEntity())
    }

    override suspend fun deleteAllFavoriteCharacters() = dao.deleteAll()
}