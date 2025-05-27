package com.guilherme.marvelcharacters.remote.service

import com.guilherme.marvelcharacters.remote.model.CharacterResponse
import com.guilherme.marvelcharacters.remote.model.Response

internal interface Service {
    suspend fun getCharacters(
        ts: String,
        hash: String,
        apiKey: String,
        nameStartsWith: String
    ): Response<CharacterResponse>
}