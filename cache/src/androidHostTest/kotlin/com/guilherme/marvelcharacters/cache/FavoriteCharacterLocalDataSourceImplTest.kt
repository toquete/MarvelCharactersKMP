package com.guilherme.marvelcharacters.cache

import com.google.common.truth.Truth.assertThat
import com.guilherme.marvelcharacters.cache.dao.FavoriteCharacterDao
import com.guilherme.marvelcharacters.cache.util.Fixtures
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class FavoriteCharacterLocalDataSourceImplTest {

    @RelaxedMockK
    private lateinit var dao: FavoriteCharacterDao

    @InjectMockKs
    private lateinit var localDataSource: FavoriteCharacterLocalDataSourceImpl

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `getFavoriteCharacters - return list of characters`() = runTest {
        coEvery { dao.getFavoriteCharacters() } returns flowOf(Fixtures.favoriteCharacterEntityList)

        val result = localDataSource.getFavoriteCharacters()

        assertThat(result.first()).containsExactly(Fixtures.character)
    }

    @Test
    fun `isCharacterFavorite - return if character is favorite`() = runTest {
        coEvery { dao.isCharacterFavorite(id = 0) } returns flowOf(true)

        val result = localDataSource.isCharacterFavorite(id = 0)

        assertThat(result.first()).isTrue()
    }

    @Test
    fun `copyFavoriteCharacter - check dao is called`() = runTest {
        localDataSource.copyFavoriteCharacter(id = 0)

        coVerify { dao.copyFavoriteCharacter(id = 0) }
    }

    @Test
    fun `deleteAll - check dao is called`() = runTest {
        localDataSource.deleteAll()

        coVerify { dao.deleteAll() }
    }

    @Test
    fun `delete - check dao is called`() = runTest {
        localDataSource.delete(id = 0)

        coVerify { dao.delete(id = 0) }
    }
}