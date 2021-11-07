package com.guilherme.marvelcharacters.data.source.local

import com.google.common.truth.Truth.assertThat
import com.guilherme.marvelcharacters.data.model.CharacterData
import com.guilherme.marvelcharacters.data.model.ImageData
import com.guilherme.marvelcharacters.data.source.local.dao.CharacterDao
import com.guilherme.marvelcharacters.data.source.local.model.CharacterEntity
import com.guilherme.marvelcharacters.data.source.local.model.ImageEntity
import com.guilherme.marvelcharacters.infrastructure.BaseUnitTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

@ExperimentalCoroutinesApi
class CharacterLocalDataSourceImplTest : BaseUnitTest() {

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

    private val character = CharacterData(
        id = 0,
        name = "Spider-Man",
        description = "",
        thumbnail = ImageData(
            path = "",
            extension = ""
        )
    )

    override fun setUp() {
        super.setUp()
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
        val characterList = listOf(character)
        coEvery { dao.getCharacterList() } returns flowOf(characterEntityList)

        val result = localDataSource.getFavoriteCharacters()

        assertThat(result.first()).isEqualTo(characterList)
    }

    @Test
    fun `insertFavoriteCharacter - check dao was called`() = runBlockingTest {
        localDataSource.insertFavoriteCharacter(character)

        coVerify { dao.insert(characterEntity) }
    }

    @Test
    fun `deleteFavoriteCharacter - check dao was called`() = runBlockingTest {
        localDataSource.deleteFavoriteCharacter(character)

        coVerify { dao.delete(characterEntity) }
    }

    @Test
    fun `deleteAllFavoriteCharacters - check dao was called`() = runBlockingTest {
        localDataSource.deleteAllFavoriteCharacters()

        coVerify { dao.deleteAll() }
    }
}