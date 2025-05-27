package com.guilherme.marvelcharacters.remote

import com.guilherme.marvelcharacters.core.model.Character
import com.guilherme.marvelcharacters.remote.model.CharacterResponse
import com.guilherme.marvelcharacters.remote.model.ContainerResponse
import com.guilherme.marvelcharacters.remote.model.ImageResponse
import com.guilherme.marvelcharacters.remote.model.Response
import com.guilherme.marvelcharacters.remote.service.Service
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertContentEquals

@ExperimentalCoroutinesApi
class CharacterRemoteDataSourceImplTest {

    private val service: Service = mock()

    private val remoteDataSource = CharacterRemoteDataSourceImpl(service)

    private val characterResponse = CharacterResponse(
        id = 0,
        name = "Spider-Man",
        description = "The Amazing Spider-Man",
        thumbnail = ImageResponse(
            path = "test",
            extension = "jpg"
        )
    )

    private val character = Character(
        id = 0,
        name = "Spider-Man",
        description = "The Amazing Spider-Man",
        thumbnail = "test.jpg"
    )

    private val response = Response(ContainerResponse(listOf(characterResponse)))

    @Test
    fun `getCharacters - returns character list on success`() = runTest {
        val characterName = "spider"
        val expected = listOf(character)

        everySuspend {
            service.getCharacters(
                ts = any(),
                hash = any(),
                apiKey = any(),
                nameStartsWith = characterName
            )
        } returns response

        val result = remoteDataSource.getCharacters(name = characterName)

        assertContentEquals(expected, result)
    }
}