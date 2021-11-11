package com.guilherme.marvelcharacters.data.repository

import com.google.common.truth.Truth.assertThat
import com.guilherme.marvelcharacters.data.mapper.CharacterDataMapper
import com.guilherme.marvelcharacters.data.model.CharacterData
import com.guilherme.marvelcharacters.data.model.ImageData
import com.guilherme.marvelcharacters.data.source.local.CharacterLocalDataSource
import com.guilherme.marvelcharacters.data.source.remote.CharacterRemoteDataSource
import com.guilherme.marvelcharacters.domain.model.Character
import com.guilherme.marvelcharacters.domain.model.Image
import com.guilherme.marvelcharacters.infrastructure.BaseUnitTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

@ExperimentalCoroutinesApi
class CharacterRepositoryImplTest : BaseUnitTest() {

    @RelaxedMockK
    private lateinit var remoteDataSource: CharacterRemoteDataSource

    @RelaxedMockK
    private lateinit var localDataSource: CharacterLocalDataSource

    private lateinit var characterRepository: CharacterRepositoryImpl

    override fun setUp() {
        super.setUp()
        characterRepository = CharacterRepositoryImpl(remoteDataSource, localDataSource, CharacterDataMapper())
    }

    @Test
    fun `getCharacters - returns character list`() = runBlockingTest {
        val characterData = CharacterData(
            id = 0,
            name = "Spider-Man",
            description = "The Amazing Spider-Man",
            thumbnail = ImageData(
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

        coEvery { remoteDataSource.getCharacters(name = "spider") } returns flowOf(listOf(characterData))

        val list = characterRepository.getCharacters("spider").first()

        assertThat(list).isEqualTo(listOf(character))
    }

    @Test
    fun `isCharacterFavorite - returns if character is favorite`() = runBlockingTest {
        coEvery { localDataSource.isCharacterFavorite(id = any()) } returns flowOf(true)

        val result = characterRepository.isCharacterFavorite(id = 0)

        assertThat(result.first()).isEqualTo(true)
    }

    @Test
    fun `getFavoriteCharacters - returns favorite characters list`() = runBlockingTest {
        val character = Character(0, "Spider-Man", "The Amazing Spider-Man", Image("", ""))
        val characterData =
            CharacterData(0, "Spider-Man", "The Amazing Spider-Man", ImageData("", ""))
        val characters = listOf(character)

        coEvery { localDataSource.getFavoriteCharacters() } returns flowOf(listOf(characterData))

        val result = characterRepository.getFavoriteCharacters()

        assertThat(result.first()).isEqualTo(characters)
    }

    @Test
    fun `insertFavoriteCharacter - check database call`() = runBlockingTest {
        val character = Character(0, "Spider-Man", "The Amazing Spider-Man", Image("", ""))
        val characterData =
            CharacterData(0, "Spider-Man", "The Amazing Spider-Man", ImageData("", ""))

        characterRepository.insertFavoriteCharacter(character)

        coVerify { localDataSource.insertFavoriteCharacter(characterData) }
    }

    @Test
    fun `deleteFavoriteCharacter - check database call`() = runBlockingTest {
        val character = Character(0, "Spider-Man", "The Amazing Spider-Man", Image("", ""))
        val characterData =
            CharacterData(0, "Spider-Man", "The Amazing Spider-Man", ImageData("", ""))

        characterRepository.deleteFavoriteCharacter(character)

        coVerify { localDataSource.deleteFavoriteCharacter(characterData) }
    }

    @Test
    fun `deleteAllFavoriteCharacters - check database call`() = runBlockingTest {
        characterRepository.deleteAllFavoriteCharacters()

        coVerify { localDataSource.deleteAllFavoriteCharacters() }
    }
}