package com.guilherme.marvelcharacters.cache

import com.google.common.truth.Truth.assertThat
import com.guilherme.marvelcharacters.cache.dao.CharacterDao
import com.guilherme.marvelcharacters.cache.model.CharacterEntity
import com.guilherme.marvelcharacters.cache.model.ImageEntity
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class CharacterLocalDataSourceImplTest {

    @RelaxedMockK
    private lateinit var dao: CharacterDao

    private lateinit var localDataSource: CharacterLocalDataSource

    private val characterEntity = CharacterEntity(
        id = 0,
        name = "Spider-Man",
        description = "",
        thumbnail = ImageEntity(
            path = "",
            extension = ""
        )
    )

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        localDataSource = CharacterLocalDataSourceImpl(dao)
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
        coEvery { dao.getCharacterList() } returns flowOf(characterEntityList)

        val result = localDataSource.getFavoriteCharacters()

        assertThat(result.first()).isEqualTo(characterEntityList)
    }

    @Test
    fun `insertFavoriteCharacter - check dao was called`() = runBlockingTest {
        localDataSource.insertFavoriteCharacter(characterEntity)

        coVerify { dao.insert(characterEntity) }
    }

    @Test
    fun `deleteFavoriteCharacter - check dao was called`() = runBlockingTest {
        localDataSource.deleteFavoriteCharacter(characterEntity)

        coVerify { dao.delete(characterEntity) }
    }

    @Test
    fun `deleteAllFavoriteCharacters - check dao was called`() = runBlockingTest {
        localDataSource.deleteAllFavoriteCharacters()

        coVerify { dao.deleteAll() }
    }
}