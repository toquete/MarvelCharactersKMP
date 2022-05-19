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
import com.guilherme.marvelcharacters.ui.detail.DetailEvent
import com.guilherme.marvelcharacters.ui.detail.DetailState
import com.guilherme.marvelcharacters.ui.detail.DetailViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
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
    fun `init - send character state`() = testCoroutineRule.runBlockingTest {
        every { isCharacterFavoriteUseCase(character.id) } returns flowOf(true)

        val detailViewModel = getViewModel()

        detailViewModel.state.test {
            assertThat(awaitItem()).isEqualTo(DetailState(isFavorite = true))
        }
    }

    @Test
    fun `onFabClick - send deleted character event`() = testCoroutineRule.runBlockingTest {
        every { isCharacterFavoriteUseCase(character.id) } returns flowOf(true)

        val detailViewModel = getViewModel()

        // simulate first collection
        val job = detailViewModel.state.launchIn(this)

        detailViewModel.onFabClick()

        coVerify { deleteFavoriteCharacterUseCase(character.id) }

        detailViewModel.event.test {
            assertThat(awaitItem()).isEqualTo(DetailEvent.ShowSnackbarMessage(R.string.character_deleted, showAction = true))
            job.cancel()
        }
    }

    @Test
    fun `onFabClick - send added character event`() = testCoroutineRule.runBlockingTest {
        every { isCharacterFavoriteUseCase(character.id) } returns flowOf(false)

        val detailViewModel = getViewModel()

        // simulate first collection
        val job = detailViewModel.state.launchIn(this)

        detailViewModel.onFabClick()

        coVerify { insertFavoriteCharacterUseCase(character) }

        detailViewModel.event.test {
            assertThat(awaitItem()).isEqualTo(DetailEvent.ShowSnackbarMessage(R.string.character_added, showAction = false))
            job.cancel()
        }
    }

    @Test
    fun `onFabClick - send generic error event`() = testCoroutineRule.runBlockingTest {
        every { isCharacterFavoriteUseCase(character.id) } returns flowOf(false)
        coEvery { insertFavoriteCharacterUseCase(character) } throws SQLiteException()

        val detailViewModel = getViewModel()

        detailViewModel.onFabClick()

        coVerify { insertFavoriteCharacterUseCase(character) }

        detailViewModel.event.test {
            assertThat(awaitItem()).isEqualTo(DetailEvent.ShowSnackbarMessage(R.string.error_message, showAction = false))
        }
    }

    @Test
    fun `onUndoClick - undo changes`() = testCoroutineRule.runBlockingTest {
        val detailViewModel = getViewModel()

        detailViewModel.onUndoClick()

        coVerify { insertFavoriteCharacterUseCase(character) }
    }

    @Test
    fun `onUndoClick - send error message on undo changes`() = testCoroutineRule.runBlockingTest {
        coEvery { insertFavoriteCharacterUseCase(character) } throws SQLiteException()

        val detailViewModel = getViewModel()

        detailViewModel.onUndoClick()

        detailViewModel.event.test {
            assertThat(awaitItem()).isEqualTo(DetailEvent.ShowSnackbarMessage(R.string.error_message, showAction = false))
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