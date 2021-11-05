package com.guilherme.marvelcharacters.data.source.remote

import com.guilherme.marvelcharacters.data.model.CharacterResponse

interface CharacterRemoteDataSource {

    suspend fun getCharacters(name: String): List<CharacterResponse>
}