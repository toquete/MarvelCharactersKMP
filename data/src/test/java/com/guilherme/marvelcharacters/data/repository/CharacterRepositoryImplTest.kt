package com.guilherme.marvelcharacters.data.repository

import com.google.common.truth.Truth.assertThat
import com.guilherme.marvelcharacters.cache.CharacterLocalDataSource
import com.guilherme.marvelcharacters.cache.model.CharacterEntity
import com.guilherme.marvelcharacters.cache.model.ImageEntity
import com.guilherme.marvelcharacters.core.model.Character
import com.guilherme.marvelcharacters.core.model.Image
import com.guilherme.marvelcharacters.data.model.CharacterData
import com.guilherme.marvelcharacters.data.model.ImageData
import com.guilherme.marvelcharacters.data.repository.infrastructure.TestCoroutineRule
import com.guilherme.marvelcharacters.data.source.remote.CharacterRemoteDataSource
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

        coEvery { remoteDataSource.getCharacters(name = "spider", any(), any()) } returns listOf(characterData)

        val list = characterRepository.getCharacters(name = "spider", key = "123", privateKey = "456")

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
        val characterEntity =
            CharacterEntity(0, "Spider-Man", "The Amazing Spider-Man", ImageEntity("", ""))
        val characters = listOf(character)

        coEvery { localDataSource.getFavoriteCharacters() } returns flowOf(listOf(characterEntity))

        val result = characterRepository.getFavoriteCharacters()

        assertThat(result.first()).isEqualTo(characters)
    }

    @Test
    fun `insertFavoriteCharacter - check database call`() = runBlockingTest {
        val character = Character(0, "Spider-Man", "The Amazing Spider-Man", Image("", ""))
        val characterEntity =
            CharacterEntity(0, "Spider-Man", "The Amazing Spider-Man", ImageEntity("", ""))

        characterRepository.insertFavoriteCharacter(character)

        coVerify { localDataSource.insertFavoriteCharacter(characterEntity) }
    }

    @Test
    fun `deleteFavoriteCharacter - check database call`() = runBlockingTest {
        val character = Character(0, "Spider-Man", "The Amazing Spider-Man", Image("", ""))
        val characterEntity =
            CharacterEntity(0, "Spider-Man", "The Amazing Spider-Man", ImageEntity("", ""))

        characterRepository.deleteFavoriteCharacter(character)

        coVerify { localDataSource.deleteFavoriteCharacter(characterEntity) }
    }

    @Test
    fun `deleteAllFavoriteCharacters - check database call`() = runBlockingTest {
        characterRepository.deleteAllFavoriteCharacters()

        coVerify { localDataSource.deleteAllFavoriteCharacters() }
    }
}