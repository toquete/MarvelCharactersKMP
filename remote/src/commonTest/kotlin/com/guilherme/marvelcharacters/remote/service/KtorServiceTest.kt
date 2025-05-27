package com.guilherme.marvelcharacters.remote.service

import com.guilherme.marvelcharacters.remote.model.CharacterResponse
import com.guilherme.marvelcharacters.remote.model.ContainerResponse
import com.guilherme.marvelcharacters.remote.model.ImageResponse
import com.guilherme.marvelcharacters.remote.model.Response
import io.ktor.client.engine.mock.*
import io.ktor.http.*
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class KtorServiceTest {

    @Test
    fun `getCharacters should return parsed data class on success`() = runTest {
        // Given
        val response = """
            {
              "data": {
                "results": [
                  {
                    "id": 0,
                    "name": "Spider-Man",
                    "description": "The Amazing Spider-Man",
                    "thumbnail": {
                      "path": "",
                      "extension": ""
                    }
                  }
                ]
              }
            }
        """.trimIndent()

        val expected = Response(
            ContainerResponse(
                listOf(
                    CharacterResponse(
                        id = 0,
                        name = "Spider-Man",
                        description = "The Amazing Spider-Man",
                        thumbnail = ImageResponse(path = "", extension = "")
                    )
                )
            )
        )
        val mockEngine = MockEngine { _ ->
            respond(
                content = response,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }
        val service = KtorService(mockEngine)

        val result = service.getCharacters(ts = "ts", hash = "hash", apiKey = "teste", nameStartsWith = "spider")
        val query = mockEngine.requestHistory.last().url.encodedQuery

        assertEquals(expected, result)
        assertEquals("ts=ts&hash=hash&apikey=teste&nameStartsWith=spider", query)
    }
}