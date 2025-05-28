package com.guilherme.marvelcharacters.cache

import com.google.common.truth.Truth.assertThat
import com.guilherme.marvelcharacters.cache.dao.CharacterDao
import com.guilherme.marvelcharacters.cache.util.Fixtures
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class CharacterLocalDataSourceImplTest {

    @RelaxedMockK
    private lateinit var dao: CharacterDao

    @InjectMockKs
    private lateinit var localDataSource: CharacterLocalDataSourceImpl

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `getCharacterById - returns character`() = runTest {
        coEvery { dao.getCharacterById(any()) } returns Fixtures.characterEntity

        val result = localDataSource.getCharacterById(id = 0)

        assertThat(result).isEqualTo(Fixtures.character)
    }

    @Test
    fun `getCharactersByName - returns character list`() = runTest {
        coEvery { dao.getCharactersByName(any()) } returns Fixtures.characterEntityList

        val result = localDataSource.getCharactersByName(name = "spider")

        assertThat(result).containsExactly(Fixtures.character)
    }

    @Test
    fun `insertAll - check if characters are inserted`() = runTest {
        localDataSource.insertAll(Fixtures.characterList)

        coVerify { dao.insertAll(Fixtures.characterEntityList) }
    }
}