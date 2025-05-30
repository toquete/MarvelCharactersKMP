package com.guilherme.marvelcharacters.remote.service

import com.guilherme.marvelcharacters.remote.model.CharacterResponse
import com.guilherme.marvelcharacters.remote.model.Response
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

internal class KtorService(engine: HttpClientEngine): Service {

    private val httpClient = HttpClient(engine) {
        defaultRequest {
            url(BASE_URL)
        }
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
            format = LoggingFormat.OkHttp
        }
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                }
            )
        }
    }

    override suspend fun getCharacters(
        ts: String,
        hash: String,
        apiKey: String,
        nameStartsWith: String
    ): Response<CharacterResponse> {
        return httpClient.get("characters") {
            url {
                parameters.apply {
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