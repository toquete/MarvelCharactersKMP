package com.guilherme.marvelcharacters.remote.service

import com.guilherme.marvelcharacters.remote.model.CharacterResponse
import com.guilherme.marvelcharacters.remote.model.Response
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

internal class KtorService(engine: HttpClientEngine) {

    private val httpClient = HttpClient(engine) {
        defaultRequest {
            url(BASE_URL)
        }
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                }
            )
        }
    }

    suspend fun getCharacters(
        ts: String,
        hash: String,
        apiKey: String,
        nameStartsWith: String
    ): Response<CharacterResponse> {
        return httpClient.get("characters") {
            url {
                parameters {
                    append("ts", ts)
                    append("hash", hash)
                    append("apikey", apiKey)
                    append("nameStartsWith", nameStartsWith)
                }
            }
        }.body()
    }

    companion object {
        private const val BASE_URL = "https://gateway.marvel.com/v1/public/"
    }
}