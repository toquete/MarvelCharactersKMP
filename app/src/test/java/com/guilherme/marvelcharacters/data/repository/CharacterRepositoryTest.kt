package com.guilherme.marvelcharacters.data.repository

import androidx.lifecycle.MutableLiveData
import com.google.common.truth.Truth.assertThat
import com.guilherme.marvelcharacters.data.model.Character
import com.guilherme.marvelcharacters.data.model.Container
import com.guilherme.marvelcharacters.data.model.Image
import com.guilherme.marvelcharacters.data.model.Result
import com.guilherme.marvelcharacters.data.source.local.dao.CharacterDao
import com.guilherme.marvelcharacters.data.source.remote.Api
import com.guilherme.marvelcharacters.infrastructure.BaseUnitTest
import com.guilherme.marvelcharacters.util.getOrAwaitValue
import com.guilherme.marvelcharacters.util.observeForTesting
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

@ExperimentalCoroutinesApi
class CharacterRepositoryTest : BaseUnitTest() {

    private lateinit var characterRepository: CharacterRepository

    @RelaxedMockK
    private lateinit var api: Api

    @RelaxedMockK
    private lateinit var characterDao: CharacterDao

    override fun setUp() {
        super.setUp()
        characterRepository = CharacterRepository(api, characterDao, testCoroutineRule.testCoroutineDispatcher)
    }

    @Test
    fun `getCharacters - retorna lista de personagens`() = runBlockingTest {
        val character = Character(0, "Spider-Man", "The Amazing Spider-Man", Image("", ""))
        val characters = listOf(character)
        val result = Result(Container(characters))

        coEvery { api.getCharacters(ts = any(), hash = any(), apiKey = any(), nameStartsWith = "spider") } returns result

        val list = characterRepository.getCharacters("spider")

        assertThat(list).isEqualTo(characters)
    }

    @Test
    fun `isCharacterFavorite - retorna se personagem é favorito`() {
        every { characterDao.isCharacterFavorite(id = any()) } returns MutableLiveData(true)

        val result = characterRepository.isCharacterFavorite(id = 0)

        result.observeForTesting {
            assertThat(result.getOrAwaitValue()).isEqualTo(true)
        }
    }

    @Test
    fun `isCharacterFavorite - retorna lista de personagens favoritos`() {
        val character = Character(0, "Spider-Man", "The Amazing Spider-Man", Image("", ""))
        val characters = listOf(character)

        every { characterDao.getCharacterList() } returns MutableLiveData(characters)

        val result = characterRepository.getFavoriteCharacters()

        result.observeForTesting {
            assertThat(result.getOrAwaitValue()).isEqualTo(characters)
        }
    }

    @Test
    fun `insertFavoriteCharacter - verifica chamada ao banco de dados`() = runBlockingTest {
        val character = Character(0, "Spider-Man", "The Amazing Spider-Man", Image("", ""))

        characterRepository.insertFavoriteCharacter(character)

        coVerify { characterDao.insert(character) }
    }

    @Test
    fun `deleteFavoriteCharacter - verifica chamada ao banco de dados`() = runBlockingTest {
        val character = Character(0, "Spider-Man", "The Amazing Spider-Man", Image("", ""))

        characterRepository.deleteFavoriteCharacter(character)

        coVerify { characterDao.delete(character) }
    }

    @Test
    fun `deleteAllFavoriteCharacters - verifica chamada ao banco de dados`() = runBlockingTest {
        val character = Character(0, "Spider-Man", "The Amazing Spider-Man", Image("", ""))

        characterRepository.deleteAllFavoriteCharacters()

        coVerify { characterDao.deleteAll() }
    }
}