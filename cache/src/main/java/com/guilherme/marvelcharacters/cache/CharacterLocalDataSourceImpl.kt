package com.guilherme.marvelcharacters.cache

import com.guilherme.marvelcharacters.cache.dao.CharacterDao
import com.guilherme.marvelcharacters.cache.extension.toEntity
import com.guilherme.marvelcharacters.cache.model.CharacterEntity
import com.guilherme.marvelcharacters.cache.model.toExternalModel
import com.guilherme.marvelcharacters.core.model.Character
import javax.inject.Inject

internal class CharacterLocalDataSourceImpl @Inject constructor(
    private val dao: CharacterDao
) : CharacterLocalDataSource {

    override suspend fun getCharacterById(id: Int): Character {
        return dao.getCharacterById(id).toExternalModel()
    }

    override suspend fun getCharactersByName(name: String): List<Character> {
        return dao.getCharactersByName(name)
            .map(CharacterEntity::toExternalModel)
    }

    override suspend fun insertAll(characters: List<Character>) {
        dao.insertAll(characters.map(Character::toEntity))
    }
}