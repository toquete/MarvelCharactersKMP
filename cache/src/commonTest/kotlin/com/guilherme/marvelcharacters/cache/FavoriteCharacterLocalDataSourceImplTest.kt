package com.guilherme.marvelcharacters.cache

import com.guilherme.marvelcharacters.cache.dao.FavoriteCharacterDao
import com.guilherme.marvelcharacters.cache.util.Fixtures
import dev.mokkery.MockMode
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
class FavoriteCharacterLocalDataSourceImplTest {

    private val dao: FavoriteCharacterDao = mock(MockMode.autofill)
    private val localDataSource = FavoriteCharacterLocalDataSourceImpl(dao)

    @Test
    fun `getFavoriteCharacters - return list of characters`() = runTest {
        every { dao.getFavoriteCharacters() } returns flowOf(Fixtures.favoriteCharacterEntityList)

        val result = localDataSource.getFavoriteCharacters()

        assertContentEquals(Fixtures.characterList, result.first())
    }

    @Test
    fun `isCharacterFavorite - return if character is favorite`() = runTest {
        every { dao.isCharacterFavorite(id = 0) } returns flowOf(true)

        val result = localDataSource.isCharacterFavorite(id = 0)

        assertTrue(result.first())
    }

    @Test
    fun `copyFavoriteCharacter - check dao is called`() = runTest {
        localDataSource.copyFavoriteCharacter(id = 0)

        verifySuspend { dao.copyFavoriteCharacter(id = 0) }
    }

    @Test
    fun `deleteAll - check dao is called`() = runTest {
        localDataSource.deleteAll()

        verifySuspend { dao.deleteAll() }
    }

    @Test
    fun `delete - check dao is called`() = runTest {
        localDataSource.delete(id = 0)

        verifySuspend { dao.delete(id = 0) }
    }
}