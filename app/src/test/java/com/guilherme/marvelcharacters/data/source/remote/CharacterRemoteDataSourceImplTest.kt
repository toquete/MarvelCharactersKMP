package com.guilherme.marvelcharacters.data.source.remote

import com.google.common.truth.Truth.assertThat
import com.guilherme.marvelcharacters.data.model.CharacterResponse
import com.guilherme.marvelcharacters.data.model.ContainerResponse
import com.guilherme.marvelcharacters.data.model.ImageResponse
import com.guilherme.marvelcharacters.data.model.Response
import com.guilherme.marvelcharacters.data.service.Api
import com.guilherme.marvelcharacters.infrastructure.BaseUnitTest
import io.mockk.coEvery
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

@ExperimentalCoroutinesApi
class CharacterRemoteDataSourceImplTest : BaseUnitTest() {

    @MockK
    private lateinit var api: Api

    @InjectMockKs
    private lateinit var remoteDataSource: CharacterRemoteDataSourceImpl

    @Test
    fun `getCharacters - returns character list on success`() = runBlockingTest {
        val characterResponse = CharacterResponse(
            id = 0,
            name = "Spider-Man",
            description = "The Amazing Spider-Man",
            thumbnail = ImageResponse(
                path = "",
                extension = ""
            )
        )
        val response = Response(ContainerResponse(listOf(characterResponse)))
        val characterName = "spider"

        coEvery { api.getCharacters(ts = any(), hash = any(), apiKey = any(), nameStartsWith = characterName) } returns response

        val result = remoteDataSource.getCharacters(name = characterName)

        assertThat(result).isEqualTo(response.container.results)
    }
}