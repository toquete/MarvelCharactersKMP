package com.guilherme.marvelcharacters.data.source.remote

import com.guilherme.marvelcharacters.data.model.Result
import kotlinx.coroutines.Deferred
import retrofit2.http.GET

interface Api {

    @GET("/v1/public/characters")
    fun getCharacters(): Deferred<Result>
}