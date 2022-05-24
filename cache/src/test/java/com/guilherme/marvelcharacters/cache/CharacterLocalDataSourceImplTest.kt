package com.guilherme.marvelcharacters.cache

import com.google.common.truth.Truth.assertThat
import com.guilherme.marvelcharacters.cache.dao.CharacterDao
import com.guilherme.marvelcharacters.cache.memory.CharacterMemoryCache
import com.guilherme.marvelcharacters.cache.model.CharacterEntity
import com.guilherme.marvelcharacters.data.model.CharacterData
import com.guilherme.marvelcharacters.data.source.local.CharacterLocalDataSource
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class CharacterLocalDataSourceImplTest {

    @RelaxedMockK
    private lateinit var dao: CharacterDao

    @RelaxedMockK
    private lateinit var cache: CharacterMemoryCache

    private lateinit var localDataSource: CharacterLocalDataSource

    private val characterEntity = CharacterEntity(
        id = 0,
        name = "Spider-Man",
        description = "",
        thumbnail = ""
    )

    private val character = CharacterData(
        id = 0,
        name = "Spider-Man",
        description = "",
        thumbnail = ""
    )

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        localDataSource = CharacterLocalDataSourceImpl(dao, cache)
    }

    @Test
    fun `isCharacterFavorite - returns true when character is favorite`() = runBlockingTest {
        coEvery { dao.isCharacterFavorite(any()) } returns flowOf(true)

        val result = localDataSource.isCharacterFavorite(id = 0)

        assertThat(result.first()).isTrue()
    }

    @Test
    fun `getFavoriteCharacters - returns favorite characters list`() = runBlockingTest {
        val characterEntityList = listOf(characterEntity)
        val characterList = listOf(character)
        coEvery { dao.getCharacterList() } returns flowOf(characterEntityList)

        val result = localDataSource.getFavoriteCharacters()

        assertThat(result.first()).isEqualTo(characterList)
    }

    @Test
    fun `getFavoriteCharacter - returns favorite character by id`() = runBlockingTest {
        every { dao.getFavoriteCharacter(id = 0) } returns flowOf(characterEntity)

        val result = localDataSource.getCharacter(id = 0)

        assertThat(result.first()).isEqualTo(character)
    }

    @Test
    fun `getFavoriteCharacter - returns cached character by id`() = runBlockingTest {
        every { cache.getCharacter(id = 0) } returns character
        every { dao.getFavoriteCharacter(id = 0) } returns flow { throw NullPointerException() }

        val result = localDataSource.getCharacter(id = 0)

        assertThat(result.first()).isEqualTo(character)
    }

    @Test
    fun `saveCache - save list in cache`() {
        val list = listOf(character)

        localDataSource.saveInCache(list)

        verify { cache.setCache(list) }
    }

    @Test
    fun `insertFavoriteCharacter - check dao was called`() = runBlockingTest {
        localDataSource.insertFavoriteCharacter(character)

        coVerify { dao.insert(characterEntity) }
    }

    @Test
    fun `deleteFavoriteCharacter - check dao was called`() = runBlockingTest {
        localDataSource.deleteFavoriteCharacter(id = 0)

        coVerify { dao.delete(id = 0) }
    }

    @Test
    fun `deleteAllFavoriteCharacters - check dao was called`() = runBlockingTest {
        localDataSource.deleteAllFavoriteCharacters()

        coVerify { dao.deleteAll() }
    }
}