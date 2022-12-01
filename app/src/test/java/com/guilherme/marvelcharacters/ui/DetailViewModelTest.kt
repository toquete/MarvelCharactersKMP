package com.guilherme.marvelcharacters.ui

import android.database.sqlite.SQLiteException
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.guilherme.marvelcharacters.R
import com.guilherme.marvelcharacters.core.model.Character
import com.guilherme.marvelcharacters.core.model.Image
import com.guilherme.marvelcharacters.domain.model.FavoriteCharacter
import com.guilherme.marvelcharacters.domain.usecase.DeleteFavoriteCharacterUseCase
import com.guilherme.marvelcharacters.domain.usecase.GetFavoriteCharacterByIdUseCase
import com.guilherme.marvelcharacters.domain.usecase.InsertFavoriteCharacterUseCase
import com.guilherme.marvelcharacters.infrastructure.BaseUnitTest
import com.guilherme.marvelcharacters.ui.detail.DetailViewModel
import com.guilherme.marvelcharacters.ui.detail.ShowSnackbar
import com.guilherme.marvelcharacters.ui.detail.Success
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
    private lateinit var getFavoriteCharacterByIdUseCase: GetFavoriteCharacterByIdUseCase

    @RelaxedMockK
    private lateinit var deleteFavoriteCharacterUseCase: DeleteFavoriteCharacterUseCase

    @RelaxedMockK
    private lateinit var insertFavoriteCharacterUseCase: InsertFavoriteCharacterUseCase

    private val character = Character(0, "Spider-Man", "The Amazing Spider-Man", Image("", ""))

    @Test
    fun `init - send character state`() = testCoroutineRule.runBlockingTest {
        val favoriteCharacter = FavoriteCharacter(character, isFavorite = true)
        every { getFavoriteCharacterByIdUseCase(character.id, any(), any()) } returns flowOf(favoriteCharacter)

        val detailViewModel = getViewModel()

        detailViewModel.uiState.test {
            assertThat(awaitItem()).isEqualTo(Success(favoriteCharacter))
        }
    }

    @Test
    fun `onFabClick - send deleted character event`() = testCoroutineRule.runBlockingTest {
        val favoriteCharacter = FavoriteCharacter(character, isFavorite = true)
        every { getFavoriteCharacterByIdUseCase(character.id, any(), any()) } returns flowOf(favoriteCharacter)

        val detailViewModel = getViewModel()

        // simulate first collection
        val job = detailViewModel.uiState.launchIn(this)

        detailViewModel.onFabClick(favoriteCharacter)

        coVerify { deleteFavoriteCharacterUseCase(character) }

        detailViewModel.uiState.test {
            assertThat(awaitItem()).isEqualTo(ShowSnackbar(R.string.character_deleted, showAction = true))
            job.cancel()
        }
    }

    @Test
    fun `onFabClick - send added character event`() = testCoroutineRule.runBlockingTest {
        val favoriteCharacter = FavoriteCharacter(character, isFavorite = false)
        every { getFavoriteCharacterByIdUseCase(character.id, any(), any()) } returns flowOf(favoriteCharacter)

        val detailViewModel = getViewModel()

        // simulate first collection
        val job = detailViewModel.uiState.launchIn(this)

        detailViewModel.onFabClick(favoriteCharacter)

        coVerify { insertFavoriteCharacterUseCase(character) }

        detailViewModel.uiState.test {
            assertThat(awaitItem()).isEqualTo(ShowSnackbar(R.string.character_added, showAction = false))
            job.cancel()
        }
    }

    @Test
    fun `onFabClick - send generic error event`() = testCoroutineRule.runBlockingTest {
        val favoriteCharacter = FavoriteCharacter(character, isFavorite = false)
        every { getFavoriteCharacterByIdUseCase(character.id, any(), any()) } returns flowOf(favoriteCharacter)
        coEvery { insertFavoriteCharacterUseCase(character) } throws SQLiteException()

        val detailViewModel = getViewModel()

        detailViewModel.onFabClick(favoriteCharacter)

        coVerify { insertFavoriteCharacterUseCase(character) }

        detailViewModel.uiState.test {
            assertThat(awaitItem()).isEqualTo(ShowSnackbar(R.string.error_message, showAction = false))
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

        detailViewModel.uiState.test {
            assertThat(awaitItem()).isEqualTo(ShowSnackbar(R.string.error_message, showAction = false))
        }
    }

    private fun getViewModel(): DetailViewModel {
        return DetailViewModel(
            character,
            getFavoriteCharacterByIdUseCase,
            deleteFavoriteCharacterUseCase,
            insertFavoriteCharacterUseCase
        )
    }
}