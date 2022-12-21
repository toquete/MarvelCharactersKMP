package com.guilherme.marvelcharacters.remote

import com.google.common.truth.Truth.assertThat
import com.guilherme.marvelcharacters.core.model.Character
import com.guilherme.marvelcharacters.remote.model.CharacterResponse
import com.guilherme.marvelcharacters.remote.model.ContainerResponse
import com.guilherme.marvelcharacters.remote.model.ImageResponse
import com.guilherme.marvelcharacters.remote.model.Response
import com.guilherme.marvelcharacters.remote.service.Api
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class CharacterRemoteDataSourceImplTest {

    @MockK
    private lateinit var api: Api

    @InjectMockKs
    private lateinit var remoteDataSource: CharacterRemoteDataSourceImpl

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

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `getCharacters - returns character list on success`() = runTest {
        val characterName = "spider"

        coEvery {
            api.getCharacters(
                ts = any(),
                hash = any(),
                apiKey = any(),
                nameStartsWith = characterName
            )
        } returns response

        val result = remoteDataSource.getCharacters(name = characterName)

        assertThat(result).isEqualTo(listOf(character))
    }
}