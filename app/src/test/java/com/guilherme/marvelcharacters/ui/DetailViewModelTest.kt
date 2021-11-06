package com.guilherme.marvelcharacters.ui

import android.database.sqlite.SQLiteException
import com.google.common.truth.Truth.assertThat
import com.guilherme.marvelcharacters.R
import com.guilherme.marvelcharacters.data.repository.CharacterRepository
import com.guilherme.marvelcharacters.domain.model.Character
import com.guilherme.marvelcharacters.domain.model.Image
import com.guilherme.marvelcharacters.infrastructure.BaseUnitTest
import com.guilherme.marvelcharacters.ui.detail.DetailViewModel
import com.guilherme.marvelcharacters.util.getOrAwaitValue
import com.guilherme.marvelcharacters.util.observeForTesting
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test

@ExperimentalCoroutinesApi
class DetailViewModelTest : BaseUnitTest() {

    @RelaxedMockK
    private lateinit var characterRepository: CharacterRepository

    private val character = Character(0, "Spider-Man", "The Amazing Spider-Man", Image("", ""))

    @Test
    fun `onFabClick - send deleted character event`() {
        coEvery { characterRepository.isCharacterFavorite(character.id) } returns true

        val detailViewModel = DetailViewModel(character, characterRepository)

        detailViewModel.snackbarMessage.observeForTesting {
            detailViewModel.onFabClick()

            coVerify { characterRepository.deleteFavoriteCharacter(character) }
            assertThat(
                detailViewModel.snackbarMessage.getOrAwaitValue().peekContent()
            ).isEqualTo(R.string.character_deleted to true)
        }
    }

    @Test
    fun `onFabClick - send added character event`() {
        coEvery { characterRepository.isCharacterFavorite(character.id) } returns false

        val detailViewModel = DetailViewModel(character, characterRepository)

        detailViewModel.snackbarMessage.observeForTesting {
            detailViewModel.onFabClick()

            coVerify { characterRepository.insertFavoriteCharacter(character) }
            assertThat(
                detailViewModel.snackbarMessage.getOrAwaitValue().peekContent()
            ).isEqualTo(R.string.character_added to false)
        }
    }

    @Test
    fun `onFabClick - send generic error event`() {
        coEvery { characterRepository.isCharacterFavorite(character.id) } returns false
        coEvery { characterRepository.insertFavoriteCharacter(character) } throws SQLiteException()

        val detailViewModel = DetailViewModel(character, characterRepository)

        detailViewModel.snackbarMessage.observeForTesting {
            detailViewModel.onFabClick()

            coVerify { characterRepository.insertFavoriteCharacter(character) }
            assertThat(
                detailViewModel.snackbarMessage.getOrAwaitValue().peekContent()
            ).isEqualTo(R.string.error_message to false)
        }
    }

    @Test
    fun `onUndoClick - undo changes`() {
        val detailViewModel = DetailViewModel(character, characterRepository)

        detailViewModel.onUndoClick()

        coVerify { characterRepository.insertFavoriteCharacter(character) }
    }

    @Test
    fun `onUndoClick - send error message on undo changes`() {
        coEvery { characterRepository.insertFavoriteCharacter(character) } throws SQLiteException()

        val detailViewModel = DetailViewModel(character, characterRepository)

        detailViewModel.snackbarMessage.observeForTesting {
            detailViewModel.onUndoClick()
            assertThat(
                detailViewModel.snackbarMessage.getOrAwaitValue().peekContent()
            ).isEqualTo(R.string.error_message to false)
        }
    }
}