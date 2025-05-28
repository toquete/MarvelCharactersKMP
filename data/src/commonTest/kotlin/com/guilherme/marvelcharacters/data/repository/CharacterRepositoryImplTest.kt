package com.guilherme.marvelcharacters.data.repository

import com.guilherme.marvelcharacters.cache.CharacterLocalDataSource
import com.guilherme.marvelcharacters.cache.FavoriteCharacterLocalDataSource
import com.guilherme.marvelcharacters.data.repository.util.Fixtures
import com.guilherme.marvelcharacters.remote.CharacterRemoteDataSource
import dev.mokkery.MockMode
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
class CharacterRepositoryImplTest {

    private val remoteDataSource: CharacterRemoteDataSource = mock(MockMode.autofill)
    private val localDataSource: CharacterLocalDataSource = mock(MockMode.autofill)
    private val favoriteLocalDataSource: FavoriteCharacterLocalDataSource = mock(MockMode.autofill)
    private val characterRepository: CharacterRepositoryImpl = CharacterRepositoryImpl(
        remoteDataSource,
        localDataSource,
        favoriteLocalDataSource
    )

    @Test
    fun `getCharacters - returns character list from remote`() = runTest {
        everySuspend { remoteDataSource.getCharacters(name = "spider") } returns Fixtures.characterList
        everySuspend { localDataSource.getCharactersByName(name = "spider") } returns emptyList()

        val list = characterRepository.getCharacters(name = "spider")

        assertContains(list, Fixtures.character)
        verifySuspend { localDataSource.insertAll(Fixtures.characterList) }
    }

    @Test
    fun `getCharacters - returns character list from local`() = runTest {
        everySuspend { localDataSource.getCharactersByName(name = "spider") } returns Fixtures.characterList

        val list = characterRepository.getCharacters(name = "spider")

        assertContains(list, Fixtures.character)
    }

    @Test
    fun `getCharacterById - returns character from local`() = runTest {
        everySuspend { localDataSource.getCharacterById(id = 0) } returns Fixtures.character

        val character = characterRepository.getCharacterById(id = 0)

        assertEquals(Fixtures.character, character)
    }

    @Test
    fun `isCharacterFavorite - returns if character is favorite`() = runTest {
        everySuspend { favoriteLocalDataSource.isCharacterFavorite(id = any()) } returns flowOf(true)

        val result = characterRepository.isCharacterFavorite(id = 0)

        assertTrue(result.first())
    }

    @Test
    fun `getFavoriteCharacters - returns favorite characters list`() = runTest {
        everySuspend { favoriteLocalDataSource.getFavoriteCharacters() } returns flowOf(Fixtures.characterList)

        val result = characterRepository.getFavoriteCharacters()

        assertContains(result.first(), Fixtures.character)
    }

    @Test
    fun `insertFavoriteCharacter - check database call`() = runTest {
        characterRepository.insertFavoriteCharacter(id = 0)

        verifySuspend { favoriteLocalDataSource.copyFavoriteCharacter(id = 0) }
    }

    @Test
    fun `deleteFavoriteCharacter - check database call`() = runTest {
        characterRepository.deleteFavoriteCharacter(id = 0)

        verifySuspend { favoriteLocalDataSource.delete(id = 0) }
    }

    @Test
    fun `deleteAllFavoriteCharacters - check database call`() = runTest {
        characterRepository.deleteAllFavoriteCharacters()

        verifySuspend { favoriteLocalDataSource.deleteAll() }
    }
}