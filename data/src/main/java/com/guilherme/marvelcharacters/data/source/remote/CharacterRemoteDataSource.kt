package com.guilherme.marvelcharacters.data.source.remote

import com.guilherme.marvelcharacters.data.model.CharacterData

interface CharacterRemoteDataSource {

    suspend fun getCharacters(
        name: String,
        key: String,
        privateKey: String
    ): List<CharacterData>
}