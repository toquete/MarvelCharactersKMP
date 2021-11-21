package com.guilherme.marvelcharacters.ui

import android.database.sqlite.SQLiteException
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.guilherme.marvelcharacters.R
import com.guilherme.marvelcharacters.domain.model.Character
import com.guilherme.marvelcharacters.domain.model.Image
import com.guilherme.marvelcharacters.domain.usecase.DeleteFavoriteCharacterUseCase
import com.guilherme.marvelcharacters.domain.usecase.InsertFavoriteCharacterUseCase
import com.guilherme.marvelcharacters.domain.usecase.IsCharacterFavoriteUseCase
import com.guilherme.marvelcharacters.infrastructure.BaseUnitTest
import com.guilherme.marvelcharacters.ui.detail.DetailViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
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
    fun `onFabClick - send deleted character event`() = runBlockingTest {
        every { isCharacterFavoriteUseCase(character.id) } returns flowOf(true)

        val detailViewModel = getViewModel()

        detailViewModel.onFabClick()

        coVerify { deleteFavoriteCharacterUseCase(character) }

        detailViewModel.events.test {
            assertThat(awaitItem()).isEqualTo(DetailViewModel.Event.ShowSnackbarMessage(R.string.character_deleted to true))
        }
    }

    @Test
    fun `onFabClick - send added character event`() = runBlockingTest {
        every { isCharacterFavoriteUseCase(character.id) } returns flowOf(false)

        val detailViewModel = getViewModel()

        detailViewModel.onFabClick()

        coVerify { insertFavoriteCharacterUseCase(character) }

        detailViewModel.events.test {
            assertThat(awaitItem()).isEqualTo(DetailViewModel.Event.ShowSnackbarMessage(R.string.character_added to false))
        }
    }

    @Test
    fun `onFabClick - send generic error event`() = runBlockingTest {
        every { isCharacterFavoriteUseCase(character.id) } returns flowOf(false)
        coEvery { insertFavoriteCharacterUseCase(character) } throws SQLiteException()

        val detailViewModel = getViewModel()

        detailViewModel.onFabClick()

        coVerify { insertFavoriteCharacterUseCase(character) }

        detailViewModel.events.test {
            assertThat(awaitItem()).isEqualTo(DetailViewModel.Event.ShowSnackbarMessage(R.string.error_message to false))
        }
    }

    @Test
    fun `onUndoClick - undo changes`() {
        val detailViewModel = getViewModel()

        detailViewModel.onUndoClick()

        coVerify { insertFavoriteCharacterUseCase(character) }
    }

    @Test
    fun `onUndoClick - send error message on undo changes`() = runBlockingTest {
        coEvery { insertFavoriteCharacterUseCase(character) } throws SQLiteException()

        val detailViewModel = getViewModel()

        detailViewModel.onUndoClick()

        detailViewModel.events.test {
            assertThat(awaitItem()).isEqualTo(DetailViewModel.Event.ShowSnackbarMessage(R.string.error_message to false))
        }
    }

    private fun getViewModel(): DetailViewModel {
        return DetailViewModel(
            character,
            isCharacterFavoriteUseCase,
            deleteFavoriteCharacterUseCase,
            insertFavoriteCharacterUseCase
        )
    }
}