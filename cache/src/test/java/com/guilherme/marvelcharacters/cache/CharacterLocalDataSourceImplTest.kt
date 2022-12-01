package com.guilherme.marvelcharacters.cache

import com.google.common.truth.Truth.assertThat
import com.guilherme.marvelcharacters.cache.dao.CharacterDao
import com.guilherme.marvelcharacters.cache.model.CharacterEntity
import com.guilherme.marvelcharacters.core.model.Character
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class CharacterLocalDataSourceImplTest {

    @RelaxedMockK
    private lateinit var dao: CharacterDao

    @InjectMockKs
    private lateinit var localDataSource: CharacterLocalDataSourceImpl

    private val characterEntity = CharacterEntity(
        id = 0,
        name = "Spider-Man",
        description = "",
        thumbnail = "test.jpg"
    )

    private val character = Character(
        id = 0,
        name = "Spider-Man",
        description = "",
        thumbnail = "test.jpg"
    )

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `getCharacterById - returns character`() = runBlockingTest {
        coEvery { dao.getCharacterById(any()) } returns characterEntity

        val result = localDataSource.getCharacterById(id = 0)

        assertThat(result).isEqualTo(character)
    }

    @Test
    fun `getCharactersByName - returns character list`() = runBlockingTest {
        coEvery { dao.getCharactersByName(any()) } returns listOf(characterEntity)

        val result = localDataSource.getCharactersByName(name = "spider")

        assertThat(result).containsExactly(character)
    }

    @Test
    fun `insertAll - check if characters are inserted`() = runBlockingTest {
        val list = listOf(characterEntity)

        localDataSource.insertAll(listOf(character))

        coVerify { dao.insertAll(list) }
    }
}