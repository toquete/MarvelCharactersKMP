package com.guilherme.marvelcharacters.remote.service

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

@ExperimentalSerializationApi
object RetrofitFactory {
    private const val BASE_URL = "https://gateway.marvel.com/v1/public/"
    private const val JSON_MEDIA_TYPE = "application/json"
    private val json = Json { ignoreUnknownKeys = true }

    fun makeRetrofitService(baseUrl: HttpUrl = BASE_URL.toHttpUrl()): Api {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(json.asConverterFactory(JSON_MEDIA_TYPE.toMediaType()))
            .build()
            .create(Api::class.java)
    }
}