package com.guilherme.marvelcharacters.cache

import com.guilherme.marvelcharacters.cache.dao.CharacterDao
import com.guilherme.marvelcharacters.cache.util.Fixtures
import dev.mokkery.MockMode
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class CharacterLocalDataSourceImplTest {

    private val dao: CharacterDao = mock(MockMode.autofill)
    private val localDataSource = CharacterLocalDataSourceImpl(dao)

    @Test
    fun `getCharacterById - returns character`() = runTest {
        everySuspend { dao.getCharacterById(any()) } returns Fixtures.characterEntity

        val result = localDataSource.getCharacterById(id = 0)

        assertEquals(Fixtures.character, result)
    }

    @Test
    fun `getCharactersByName - returns character list`() = runTest {
        everySuspend { dao.getCharactersByName(any()) } returns Fixtures.characterEntityList

        val result = localDataSource.getCharactersByName(name = "spider")

        assertContentEquals(Fixtures.characterList, result)
    }

    @Test
    fun `insertAll - check if characters are inserted`() = runTest {
        localDataSource.insertAll(Fixtures.characterList)

        verifySuspend { dao.insertAll(Fixtures.characterEntityList) }
    }
}