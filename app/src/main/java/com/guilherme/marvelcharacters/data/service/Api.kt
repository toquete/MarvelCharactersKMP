package com.guilherme.marvelcharacters.data.service

import com.guilherme.marvelcharacters.data.model.CharacterResponse
import com.guilherme.marvelcharacters.data.model.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {

    @GET("characters")
    suspend fun getCharacters(
        @Query("ts") ts: String,
        @Query("hash") hash: String,
        @Query("apikey") apiKey: String,
        @Query("nameStartsWith") nameStartsWith: String
    ): Response<CharacterResponse>
}