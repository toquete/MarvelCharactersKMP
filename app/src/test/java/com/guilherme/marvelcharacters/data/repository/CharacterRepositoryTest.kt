package com.guilherme.marvelcharacters.data.repository

import androidx.lifecycle.MutableLiveData
import com.google.common.truth.Truth.assertThat
import com.guilherme.marvelcharacters.data.model.Character
import com.guilherme.marvelcharacters.data.model.CharacterResponse
import com.guilherme.marvelcharacters.data.model.Image
import com.guilherme.marvelcharacters.data.model.ImageResponse
import com.guilherme.marvelcharacters.data.source.local.dao.CharacterDao
import com.guilherme.marvelcharacters.data.source.remote.CharacterRemoteDataSource
import com.guilherme.marvelcharacters.infrastructure.BaseUnitTest
import com.guilherme.marvelcharacters.util.getOrAwaitValue
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
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
    private lateinit var characterDao: CharacterDao

    @InjectMockKs
    private lateinit var characterRepository: CharacterRepository

    @Test
    fun `getCharacters - returns character list`() = runBlockingTest {
        val characterResponse = CharacterResponse(
            id = 0,
            name = "Spider-Man",
            description = "The Amazing Spider-Man",
            thumbnail = ImageResponse(
                path = "",
                extension = ""
            )
        )
        val characterList = listOf(characterResponse.toCharacter())

        coEvery { remoteDataSource.getCharacters(name = "spider") } returns listOf(characterResponse)

        val list = characterRepository.getCharacters("spider")

        assertThat(list).isEqualTo(characterList)
    }

    @Test
    fun `isCharacterFavorite - returns if character is favorite`() {
        every { characterDao.isCharacterFavorite(id = any()) } returns MutableLiveData(true)

        val result = characterRepository.isCharacterFavorite(id = 0)

        assertThat(result.getOrAwaitValue()).isEqualTo(true)
    }

    @Test
    fun `isCharacterFavorite - returns favorite characters list`() {
        val character = Character(0, "Spider-Man", "The Amazing Spider-Man", Image("", ""))
        val characters = listOf(character)

        every { characterDao.getCharacterList() } returns MutableLiveData(characters)

        val result = characterRepository.getFavoriteCharacters()

        assertThat(result.getOrAwaitValue()).isEqualTo(characters)
    }

    @Test
    fun `insertFavoriteCharacter - check database call`() = runBlockingTest {
        val character = Character(0, "Spider-Man", "The Amazing Spider-Man", Image("", ""))

        characterRepository.insertFavoriteCharacter(character)

        coVerify { characterDao.insert(character) }
    }

    @Test
    fun `deleteFavoriteCharacter - check database call`() = runBlockingTest {
        val character = Character(0, "Spider-Man", "The Amazing Spider-Man", Image("", ""))

        characterRepository.deleteFavoriteCharacter(character)

        coVerify { characterDao.delete(character) }
    }

    @Test
    fun `deleteAllFavoriteCharacters - check database call`() = runBlockingTest {
        characterRepository.deleteAllFavoriteCharacters()

        coVerify { characterDao.deleteAll() }
    }
}