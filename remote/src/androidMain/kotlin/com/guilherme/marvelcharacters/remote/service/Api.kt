package com.guilherme.marvelcharacters.remote.service

import com.guilherme.marvelcharacters.remote.model.CharacterResponse
import com.guilherme.marvelcharacters.remote.model.Response
import retrofit2.http.GET
import retrofit2.http.Query

internal interface Api {

    @GET("characters")
    suspend fun getCharacters(
        @Query("ts") ts: String,
        @Query("hash") hash: String,
        @Query("apikey") apiKey: String,
        @Query("nameStartsWith") nameStartsWith: String
    ): Response<CharacterResponse>
}