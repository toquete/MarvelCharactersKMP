package com.guilherme.marvelcharacters.data.repository

import com.google.common.truth.Truth.assertThat
import com.guilherme.marvelcharacters.data.source.local.CharacterLocalDataSource
import com.guilherme.marvelcharacters.data.source.remote.CharacterRemoteDataSource
import com.guilherme.marvelcharacters.domain.model.Character
import com.guilherme.marvelcharacters.domain.model.Image
import com.guilherme.marvelcharacters.infrastructure.BaseUnitTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

@ExperimentalCoroutinesApi
class CharacterRepositoryTest : BaseUnitTest() {

    @RelaxedMockK
    private lateinit var remoteDataSource: CharacterRemoteDataSource

    @RelaxedMockK
    private lateinit var localDataSource: CharacterLocalDataSource

    @InjectMockKs
    private lateinit var characterRepository: CharacterRepository

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
        val characterList = listOf(character)

        coEvery { remoteDataSource.getCharacters(name = "spider") } returns listOf(character)

        val list = characterRepository.getCharacters("spider")

        assertThat(list).isEqualTo(characterList)
    }

    @Test
    fun `isCharacterFavorite - returns if character is favorite`() = runBlockingTest {
        coEvery { localDataSource.isCharacterFavorite(id = any()) } returns true

        val result = characterRepository.isCharacterFavorite(id = 0)

        assertThat(result).isEqualTo(true)
    }

    @Test
    fun `isCharacterFavorite - returns favorite characters list`() = runBlockingTest {
        val character = Character(0, "Spider-Man", "The Amazing Spider-Man", Image("", ""))
        val characters = listOf(character)

        coEvery { localDataSource.getFavoriteCharacters() } returns characters

        val result = characterRepository.getFavoriteCharacters()

        assertThat(result).isEqualTo(characters)
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