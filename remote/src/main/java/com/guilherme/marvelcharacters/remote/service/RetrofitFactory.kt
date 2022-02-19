package com.guilherme.marvelcharacters.remote.service

import okhttp3.HttpUrl
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitFactory {
    private const val BASE_URL = "https://gateway.marvel.com/v1/public/"

    fun makeRetrofitService(baseUrl: HttpUrl = HttpUrl.get(BASE_URL)): Api {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(Api::class.java)
    }
}