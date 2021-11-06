package com.guilherme.marvelcharacters.data.source.remote

import com.google.common.truth.Truth.assertThat
import com.guilherme.marvelcharacters.data.service.Api
import com.guilherme.marvelcharacters.data.source.remote.model.CharacterResponse
import com.guilherme.marvelcharacters.data.source.remote.model.ContainerResponse
import com.guilherme.marvelcharacters.data.source.remote.model.ImageResponse
import com.guilherme.marvelcharacters.data.source.remote.model.Response
import com.guilherme.marvelcharacters.domain.model.Character
import com.guilherme.marvelcharacters.domain.model.Image
import com.guilherme.marvelcharacters.infrastructure.BaseUnitTest
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

@ExperimentalCoroutinesApi
class CharacterRemoteDataSourceImplTest : BaseUnitTest() {

    @MockK
    private lateinit var api: Api

    private lateinit var remoteDataSource: CharacterRemoteDataSourceImpl

    override fun setUp() {
        super.setUp()
        remoteDataSource = CharacterRemoteDataSourceImpl(api)
    }

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
        val character = Character(
            id = 0,
            name = "Spider-Man",
            description = "The Amazing Spider-Man",
            thumbnail = Image(
                path = "",
                extension = ""
            )
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

        val result = remoteDataSource.getCharacters(name = characterName)

        assertThat(result).isEqualTo(listOf(character))
    }
}