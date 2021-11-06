package com.guilherme.marvelcharacters.data.source.remote

import com.guilherme.marvelcharacters.domain.model.Character

interface CharacterRemoteDataSource {

    suspend fun getCharacters(name: String): List<Character>
}