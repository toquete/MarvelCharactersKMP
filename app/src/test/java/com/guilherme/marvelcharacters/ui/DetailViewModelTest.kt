package com.guilherme.marvelcharacters.ui

import android.database.sqlite.SQLiteException
import com.google.common.truth.Truth.assertThat
import com.guilherme.marvelcharacters.R
import com.guilherme.marvelcharacters.domain.model.Character
import com.guilherme.marvelcharacters.domain.model.Image
import com.guilherme.marvelcharacters.domain.usecase.DeleteFavoriteCharacterUseCase
import com.guilherme.marvelcharacters.domain.usecase.InsertFavoriteCharacterUseCase
import com.guilherme.marvelcharacters.domain.usecase.IsCharacterFavoriteUseCase
import com.guilherme.marvelcharacters.infrastructure.BaseUnitTest
import com.guilherme.marvelcharacters.ui.detail.DetailViewModel
import com.guilherme.marvelcharacters.util.getOrAwaitValue
import com.guilherme.marvelcharacters.util.observeForTesting
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import org.junit.Test

@ExperimentalCoroutinesApi
class DetailViewModelTest : BaseUnitTest() {

    @RelaxedMockK
    private lateinit var isCharacterFavoriteUseCase: IsCharacterFavoriteUseCase

    @RelaxedMockK
    private lateinit var deleteFavoriteCharacterUseCase: DeleteFavoriteCharacterUseCase

    @RelaxedMockK
    private lateinit var insertFavoriteCharacterUseCase: InsertFavoriteCharacterUseCase

    private val character = Character(0, "Spider-Man", "The Amazing Spider-Man", Image("", ""))

    @Test
    fun `onFabClick - send deleted character event`() {
        every { isCharacterFavoriteUseCase(character.id) } returns flowOf(true)

        val detailViewModel = getViewModel()

        detailViewModel.snackbarMessage.observeForTesting {
            detailViewModel.onFabClick()

            coVerify { deleteFavoriteCharacterUseCase(character) }
            assertThat(
                detailViewModel.snackbarMessage.getOrAwaitValue().peekContent()
            ).isEqualTo(R.string.character_deleted to true)
        }
    }

    @Test
    fun `onFabClick - send added character event`() {
        every { isCharacterFavoriteUseCase(character.id) } returns flowOf(false)

        val detailViewModel = getViewModel()

        detailViewModel.snackbarMessage.observeForTesting {
            detailViewModel.onFabClick()

            coVerify { insertFavoriteCharacterUseCase(character) }
            assertThat(
                detailViewModel.snackbarMessage.getOrAwaitValue().peekContent()
            ).isEqualTo(R.string.character_added to false)
        }
    }

    @Test
    fun `onFabClick - send generic error event`() {
        every { isCharacterFavoriteUseCase(character.id) } returns flowOf(false)
        coEvery { insertFavoriteCharacterUseCase(character) } throws SQLiteException()

        val detailViewModel = getViewModel()

        detailViewModel.snackbarMessage.observeForTesting {
            detailViewModel.onFabClick()

            coVerify { insertFavoriteCharacterUseCase(character) }
            assertThat(
                detailViewModel.snackbarMessage.getOrAwaitValue().peekContent()
            ).isEqualTo(R.string.error_message to false)
        }
    }

    @Test
    fun `onUndoClick - undo changes`() {
        val detailViewModel = getViewModel()

        detailViewModel.onUndoClick()

        coVerify { insertFavoriteCharacterUseCase(character) }
    }

    @Test
    fun `onUndoClick - send error message on undo changes`() {
        coEvery { insertFavoriteCharacterUseCase(character) } throws SQLiteException()

        val detailViewModel = getViewModel()

        detailViewModel.snackbarMessage.observeForTesting {
            detailViewModel.onUndoClick()
            assertThat(
                detailViewModel.snackbarMessage.getOrAwaitValue().peekContent()
            ).isEqualTo(R.string.error_message to false)
        }
    }

    private fun getViewModel(): DetailViewModel {
        return DetailViewModel(
            character,
            isCharacterFavoriteUseCase,
            deleteFavoriteCharacterUseCase,
            insertFavoriteCharacterUseCase,
            testCoroutineRule.testCoroutineDispatcher
        )
    }
}