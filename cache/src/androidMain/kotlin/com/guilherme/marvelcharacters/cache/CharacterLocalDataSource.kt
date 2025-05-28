package com.guilherme.marvelcharacters.cache

import com.guilherme.marvelcharacters.core.model.Character

interface CharacterLocalDataSource {

    suspend fun getCharacterById(id: Int): Character

    suspend fun getCharactersByName(name: String): List<Character>

    suspend fun insertAll(characters: List<Character>)
}