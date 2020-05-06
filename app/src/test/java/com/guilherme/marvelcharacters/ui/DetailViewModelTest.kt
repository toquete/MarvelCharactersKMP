package com.guilherme.marvelcharacters.ui

import android.database.sqlite.SQLiteException
import androidx.lifecycle.MutableLiveData
import com.google.common.truth.Truth.assertThat
import com.guilherme.marvelcharacters.R
import com.guilherme.marvelcharacters.data.model.Character
import com.guilherme.marvelcharacters.data.model.Image
import com.guilherme.marvelcharacters.data.repository.CharacterRepository
import com.guilherme.marvelcharacters.infrastructure.BaseUnitTest
import com.guilherme.marvelcharacters.ui.detail.DetailViewModel
import com.guilherme.marvelcharacters.util.getOrAwaitValue
import com.guilherme.marvelcharacters.util.observeForTesting
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test

@ExperimentalCoroutinesApi
class DetailViewModelTest : BaseUnitTest() {

    @RelaxedMockK
    private lateinit var characterRepository: CharacterRepository

    private val character = Character(0, "Spider-Man", "The Amazing Spider-Man", Image("", ""))

    @Test
    fun `onFabClick - envia evento de personagem removido`() = testCoroutineRule.runBlockingTest {
        every { characterRepository.isCharacterFavorite(character.id) } returns MutableLiveData(true)

        val detailViewModel = DetailViewModel(character, characterRepository, testCoroutineRule.testCoroutineDispatcher)

        detailViewModel.snackbarMessage.observeForTesting {
            detailViewModel.onFabClick()

            coVerify { characterRepository.deleteFavoriteCharacter(character) }
            assertThat(detailViewModel.snackbarMessage.getOrAwaitValue().peekContent()).isEqualTo(R.string.character_deleted to true)
        }
    }

    @Test
    fun `onFabClick - envia evento de personagem adicionado`() = testCoroutineRule.runBlockingTest {
        every { characterRepository.isCharacterFavorite(character.id) } returns MutableLiveData(false)

        val detailViewModel = DetailViewModel(character, characterRepository, testCoroutineRule.testCoroutineDispatcher)

        detailViewModel.snackbarMessage.observeForTesting {
            detailViewModel.onFabClick()

            coVerify { characterRepository.insertFavoriteCharacter(character) }
            assertThat(detailViewModel.snackbarMessage.getOrAwaitValue().peekContent()).isEqualTo(R.string.character_added to false)
        }
    }

    @Test
    fun `onFabClick - envia evento de erro quando query falha`() = testCoroutineRule.runBlockingTest {
        every { characterRepository.isCharacterFavorite(character.id) } returns MutableLiveData(null)

        val detailViewModel = DetailViewModel(character, characterRepository, testCoroutineRule.testCoroutineDispatcher)

        detailViewModel.snackbarMessage.observeForTesting {
            detailViewModel.onFabClick()

            assertThat(detailViewModel.snackbarMessage.getOrAwaitValue().peekContent()).isEqualTo(R.string.unknown_character to false)
        }
    }

    @Test()
    fun `onFabClick - envia evento de erro genérico`() = testCoroutineRule.runBlockingTest {
        every { characterRepository.isCharacterFavorite(character.id) } returns MutableLiveData(false)
        coEvery { characterRepository.insertFavoriteCharacter(character) } throws SQLiteException()

        val detailViewModel = DetailViewModel(character, characterRepository, testCoroutineRule.testCoroutineDispatcher)

        detailViewModel.snackbarMessage.observeForTesting {
            detailViewModel.onFabClick()

            coVerify { characterRepository.insertFavoriteCharacter(character) }
            assertThat(detailViewModel.snackbarMessage.getOrAwaitValue().peekContent()).isEqualTo(R.string.error_message to false)
        }
    }

    @Test
    fun `onUndoClick - desfaz alterações`() = testCoroutineRule.runBlockingTest {
        val detailViewModel = DetailViewModel(character, characterRepository, testCoroutineRule.testCoroutineDispatcher)

        detailViewModel.onUndoClick()

        coVerify { characterRepository.insertFavoriteCharacter(character) }
    }

    @Test
    fun `onUndoClick - envia mensagem de erro ao desfazer alterações`() = testCoroutineRule.runBlockingTest {
        coEvery { characterRepository.insertFavoriteCharacter(character) } throws SQLiteException()

        val detailViewModel = DetailViewModel(character, characterRepository, testCoroutineRule.testCoroutineDispatcher)

        detailViewModel.snackbarMessage.observeForTesting {
            detailViewModel.onUndoClick()
            assertThat(detailViewModel.snackbarMessage.getOrAwaitValue().peekContent()).isEqualTo(R.string.error_message to false)
        }
    }
}