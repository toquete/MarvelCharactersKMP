package com.guilherme.marvelcharacters.remote.service

import com.google.common.truth.Truth.assertThat
import com.guilherme.marvelcharacters.remote.model.CharacterResponse
import com.guilherme.marvelcharacters.remote.model.ContainerResponse
import com.guilherme.marvelcharacters.remote.model.ImageResponse
import com.guilherme.marvelcharacters.remote.model.Response
import com.guilherme.marvelcharacters.remote.util.readFile
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.ExperimentalSerializationApi
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Test

@ExperimentalCoroutinesApi
@ExperimentalSerializationApi
class ApiTest {

    private val mockWebServer = MockWebServer()
    private val api = RetrofitFactory.makeRetrofitService(baseUrl = mockWebServer.url(""))

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `getCharacters should return parsed data class on success`() = runTest {
        // Given
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
        val response = MockResponse().setBody(readFile("get_characters_200.json"))
        mockWebServer.enqueue(response)

        // When
        val result = api.getCharacters(ts = "ts", hash = "hash", apiKey = "teste", nameStartsWith = "spider")

        // Then
        val request = mockWebServer.takeRequest()
        assertThat(result).isEqualTo(expected)
        assertThat(request.path).isEqualTo("/characters?ts=ts&hash=hash&apikey=teste&nameStartsWith=spider")
    }
}