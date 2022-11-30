package com.guilherme.marvelcharacters.data.repository

import com.google.common.truth.Truth.assertThat
import com.guilherme.marvelcharacters.cache.CharacterLocalDataSource
import com.guilherme.marvelcharacters.core.model.Character
import com.guilherme.marvelcharacters.core.model.Image
import com.guilherme.marvelcharacters.data.repository.infrastructure.TestCoroutineRule
import com.guilherme.marvelcharacters.remote.CharacterRemoteDataSource
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class CharacterRepositoryImplTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @RelaxedMockK
    private lateinit var remoteDataSource: CharacterRemoteDataSource

    @RelaxedMockK
    private lateinit var localDataSource: CharacterLocalDataSource

    private lateinit var characterRepository: CharacterRepositoryImpl

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        characterRepository = CharacterRepositoryImpl(remoteDataSource, localDataSource, testCoroutineRule.testCoroutineDispatcher)
    }

    @Test
    fun `getCharacters - returns character list`() = runBlockingTest {
        val character = Character(
            id = 0,
            name = "Spider-Man",
            description = "The Amazing Spider-Man",
            thumbnail = Image(
                path = "",
                extension = ""
            )
        )

        coEvery { remoteDataSource.getCharacters(name = "spider", any(), any()) } returns listOf(character)

        val list = characterRepository.getCharacters(name = "spider", key = "123", privateKey = "456")

        assertThat(list).isEqualTo(listOf(character))
    }

    @Test
    fun `getCharacterById - returns character`() = runBlockingTest {
        val character = Character(
            id = 0,
            name = "Spider-Man",
            description = "The Amazing Spider-Man",
            thumbnail = Image(
                path = "",
                extension = ""
            )
        )

        coEvery { remoteDataSource.getCharacterById(id = 0, any(), any()) } returns character

        val list = characterRepository.getCharacterById(id = 0, key = "123", privateKey = "456")

        assertThat(list).isEqualTo(character)
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
        val characters = listOf(character)

        coEvery { localDataSource.getFavoriteCharacters() } returns flowOf(characters)

        val result = characterRepository.getFavoriteCharacters()

        assertThat(result.first()).isEqualTo(characters)
    }

    @Test
    fun `insertFavoriteCharacter - check database call`() = runBlockingTest {
        val character = Character(0, "Spider-Man", "The Amazing Spider-Man", Image("", ""))

        characterRepository.insertFavoriteCharacter(character)

        coVerify { localDataSource.insertFavoriteCharacter(character) }
    }

    @Test
    fun `deleteFavoriteCharacter - check database call`() = runBlockingTest {
        val character = Character(0, "Spider-Man", "The Amazing Spider-Man", Image("", ""))

        characterRepository.deleteFavoriteCharacter(character)

        coVerify { localDataSource.deleteFavoriteCharacter(character) }
    }

    @Test
    fun `deleteAllFavoriteCharacters - check database call`() = runBlockingTest {
        characterRepository.deleteAllFavoriteCharacters()

        coVerify { localDataSource.deleteAllFavoriteCharacters() }
    }
}