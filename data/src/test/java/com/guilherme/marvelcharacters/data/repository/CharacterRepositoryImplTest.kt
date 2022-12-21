package com.guilherme.marvelcharacters.data.repository

import com.google.common.truth.Truth.assertThat
import com.guilherme.marvelcharacters.cache.CharacterLocalDataSource
import com.guilherme.marvelcharacters.cache.FavoriteCharacterLocalDataSource
import com.guilherme.marvelcharacters.data.repository.util.Fixtures
import com.guilherme.marvelcharacters.remote.CharacterRemoteDataSource
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class CharacterRepositoryImplTest {

    private val testDispatcher = StandardTestDispatcher()

    @RelaxedMockK
    private lateinit var remoteDataSource: CharacterRemoteDataSource

    @RelaxedMockK
    private lateinit var localDataSource: CharacterLocalDataSource

    @RelaxedMockK
    private lateinit var favoriteLocalDataSource: FavoriteCharacterLocalDataSource

    private lateinit var characterRepository: CharacterRepositoryImpl

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        characterRepository = CharacterRepositoryImpl(
            remoteDataSource,
            localDataSource,
            favoriteLocalDataSource,
            testDispatcher
        )
    }

    @Test
    fun `getCharacters - returns character list from remote`() = runTest(testDispatcher.scheduler) {
        coEvery { remoteDataSource.getCharacters(name = "spider") } returns Fixtures.characterList
        coEvery { localDataSource.getCharactersByName(name = "spider") } returns emptyList()

        val list = characterRepository.getCharacters(name = "spider")

        assertThat(list).containsExactly(Fixtures.character)
        coVerify { localDataSource.insertAll(Fixtures.characterList) }
    }

    @Test
    fun `getCharacters - returns character list from local`() = runTest(testDispatcher.scheduler) {
        coEvery { localDataSource.getCharactersByName(name = "spider") } returns Fixtures.characterList

        val list = characterRepository.getCharacters(name = "spider")

        assertThat(list).containsExactly(Fixtures.character)
    }

    @Test
    fun `getCharacterById - returns character from local`() = runTest(testDispatcher.scheduler) {
        coEvery { localDataSource.getCharacterById(id = 0) } returns Fixtures.character

        val list = characterRepository.getCharacterById(id = 0)

        assertThat(list).isEqualTo(Fixtures.character)
    }

    @Test
    fun `isCharacterFavorite - returns if character is favorite`() = runTest(testDispatcher.scheduler) {
        coEvery { favoriteLocalDataSource.isCharacterFavorite(id = any()) } returns flowOf(true)

        val result = characterRepository.isCharacterFavorite(id = 0)

        assertThat(result.first()).isTrue()
    }

    @Test
    fun `getFavoriteCharacters - returns favorite characters list`() = runTest(testDispatcher.scheduler) {
        coEvery { favoriteLocalDataSource.getFavoriteCharacters() } returns flowOf(Fixtures.characterList)

        val result = characterRepository.getFavoriteCharacters()

        assertThat(result.first()).containsExactly(Fixtures.character)
    }

    @Test
    fun `insertFavoriteCharacter - check database call`() = runTest(testDispatcher.scheduler) {
        characterRepository.insertFavoriteCharacter(id = 0)

        coVerify { favoriteLocalDataSource.copyFavoriteCharacter(id = 0) }
    }

    @Test
    fun `deleteFavoriteCharacter - check database call`() = runTest(testDispatcher.scheduler) {
        characterRepository.deleteFavoriteCharacter(id = 0)

        coVerify { favoriteLocalDataSource.delete(id = 0) }
    }

    @Test
    fun `deleteAllFavoriteCharacters - check database call`() = runTest(testDispatcher.scheduler) {
        characterRepository.deleteAllFavoriteCharacters()

        coVerify { favoriteLocalDataSource.deleteAll() }
    }
}