package com.guilherme.marvelcharacters.remote

import com.guilherme.marvelcharacters.core.model.Character

interface CharacterRemoteDataSource {

    suspend fun getCharacters(name: String): List<Character>
}