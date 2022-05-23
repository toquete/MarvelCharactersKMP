package com.guilherme.marvelcharacters.remote

import com.google.common.truth.Truth.assertThat
import com.guilherme.marvelcharacters.data.model.CharacterData
import com.guilherme.marvelcharacters.remote.model.CharacterResponse
import com.guilherme.marvelcharacters.remote.model.ContainerResponse
import com.guilherme.marvelcharacters.remote.model.ImageResponse
import com.guilherme.marvelcharacters.remote.model.Response
import com.guilherme.marvelcharacters.remote.service.Api
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class CharacterRemoteDataSourceImplTest {

    @MockK
    private lateinit var api: Api

    private lateinit var remoteDataSource: CharacterRemoteDataSourceImpl

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        remoteDataSource = CharacterRemoteDataSourceImpl(api)
    }

    @Test
    fun `getCharacters - returns character list on success`() = runBlockingTest {
        val characterResponse = CharacterResponse(
            id = 0,
            name = "Spider-Man",
            description = "The Amazing Spider-Man",
            thumbnail = ImageResponse(
                path = "path",
                extension = "jpg"
            )
        )
        val character = CharacterData(
            id = 0,
            name = "Spider-Man",
            description = "The Amazing Spider-Man",
            thumbnail = "path.jpg"
        )
        val response = Response(ContainerResponse(listOf(characterResponse)))
        val characterName = "spider"

        coEvery {
            api.getCharacters(
                ts = any(),
                hash = any(),
                apiKey = any(),
                nameStartsWith = characterName
            )
        } returns response

        val result = remoteDataSource.getCharacters(name = characterName, key = "123", privateKey = "456")

        assertThat(result.first()).isEqualTo(listOf(character))
    }
}