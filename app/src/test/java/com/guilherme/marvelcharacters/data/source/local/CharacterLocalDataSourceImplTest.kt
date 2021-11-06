package com.guilherme.marvelcharacters.data.source.local

import com.google.common.truth.Truth.assertThat
import com.guilherme.marvelcharacters.data.model.CharacterEntity
import com.guilherme.marvelcharacters.data.model.ImageEntity
import com.guilherme.marvelcharacters.data.source.local.dao.CharacterDao
import com.guilherme.marvelcharacters.domain.model.Character
import com.guilherme.marvelcharacters.domain.model.Image
import com.guilherme.marvelcharacters.infrastructure.BaseUnitTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
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

    private val character = Character(
        id = 0,
        name = "Spider-Man",
        description = "",
        thumbnail = Image(
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
        coEvery { dao.isCharacterFavorite(any()) } returns true

        val result = localDataSource.isCharacterFavorite(id = 0)

        assertThat(result).isTrue()
    }

    @Test
    fun `getFavoriteCharacters - returns favorite characters list`() = runBlockingTest {
        val characterEntityList = listOf(characterEntity)
        val characterList = listOf(character)
        coEvery { dao.getCharacterList() } returns characterEntityList

        val result = localDataSource.getFavoriteCharacters()

        assertThat(result).isEqualTo(characterList)
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