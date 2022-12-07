package com.guilherme.marvelcharacters.feature.detail

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.guilherme.marvelcharacters.core.testing.util.BaseUnitTest
import com.guilherme.marvelcharacters.domain.model.FavoriteCharacter
import com.guilherme.marvelcharacters.domain.usecase.GetFavoriteCharacterByIdUseCase
import com.guilherme.marvelcharacters.domain.usecase.ToggleFavoriteCharacterUseCase
import com.guilherme.marvelcharacters.feature.detail.util.Fixtures
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import org.junit.Test
import java.io.IOException

@ExperimentalCoroutinesApi
class DetailViewModelTest : BaseUnitTest() {

    @RelaxedMockK
    private lateinit var getFavoriteCharacterByIdUseCase: GetFavoriteCharacterByIdUseCase

    @RelaxedMockK
    private lateinit var toggleFavoriteCharacterUseCase: ToggleFavoriteCharacterUseCase

    private val savedStateHandle = SavedStateHandle(mapOf("characterId" to Fixtures.character.id))

    @Test
    fun `init - send character state`() = testCoroutineRule.runBlockingTest {
        val favoriteCharacter = FavoriteCharacter(Fixtures.character, isFavorite = true)
        every { getFavoriteCharacterByIdUseCase(Fixtures.character.id) } returns flowOf(favoriteCharacter)

        val detailViewModel = getViewModel()

        detailViewModel.uiState.test {
            assertThat(awaitItem()).isEqualTo(DetailUiState.Success(favoriteCharacter))
        }
    }

    @Test
    fun `onFabClick - send deleted character event`() = testCoroutineRule.runBlockingTest {
        val favoriteCharacter = FavoriteCharacter(Fixtures.character, isFavorite = true)
        every { getFavoriteCharacterByIdUseCase(Fixtures.character.id) } returns flowOf(favoriteCharacter)

        val detailViewModel = getViewModel()

        // simulate first collection
        val job = detailViewModel.uiState.launchIn(this)

        detailViewModel.onFabClick(isFavorite = true)

        coVerify { toggleFavoriteCharacterUseCase(Fixtures.character.id, isFavorite = true) }

        detailViewModel.uiState.test {
            assertThat(awaitItem()).isEqualTo(DetailUiState.ShowSnackbar(R.string.character_deleted, showAction = true))
            job.cancel()
        }
    }

    @Test
    fun `onFabClick - send added character event`() = testCoroutineRule.runBlockingTest {
        val favoriteCharacter = FavoriteCharacter(Fixtures.character, isFavorite = false)
        every { getFavoriteCharacterByIdUseCase(Fixtures.character.id) } returns flowOf(favoriteCharacter)

        val detailViewModel = getViewModel()

        // simulate first collection
        val job = detailViewModel.uiState.launchIn(this)

        detailViewModel.onFabClick(isFavorite = false)

        coVerify { toggleFavoriteCharacterUseCase(Fixtures.character.id, isFavorite = false) }

        detailViewModel.uiState.test {
            assertThat(awaitItem()).isEqualTo(DetailUiState.ShowSnackbar(R.string.character_added, showAction = false))
            job.cancel()
        }
    }

    @Test
    fun `onFabClick - send generic error event`() = testCoroutineRule.runBlockingTest {
        val favoriteCharacter = FavoriteCharacter(Fixtures.character, isFavorite = false)
        every { getFavoriteCharacterByIdUseCase(Fixtures.character.id) } returns flowOf(favoriteCharacter)
        coEvery { toggleFavoriteCharacterUseCase(Fixtures.character.id, isFavorite = false) } throws IOException()

        val detailViewModel = getViewModel()

        detailViewModel.onFabClick(isFavorite = false)

        coVerify { toggleFavoriteCharacterUseCase(Fixtures.character.id, isFavorite = false) }

        detailViewModel.uiState.test {
            assertThat(awaitItem()).isEqualTo(DetailUiState.ShowSnackbar(R.string.error_message, showAction = false))
        }
    }

    @Test
    fun `onUndoClick - undo changes`() = testCoroutineRule.runBlockingTest {
        val detailViewModel = getViewModel()

        detailViewModel.onUndoClick()

        coVerify { toggleFavoriteCharacterUseCase(Fixtures.character.id, isFavorite = false) }
    }

    @Test
    fun `onUndoClick - send error message on undo changes`() = testCoroutineRule.runBlockingTest {
        coEvery { toggleFavoriteCharacterUseCase(Fixtures.character.id, isFavorite = false) } throws IOException()

        val detailViewModel = getViewModel()

        detailViewModel.onUndoClick()

        detailViewModel.uiState.test {
            assertThat(awaitItem()).isEqualTo(DetailUiState.ShowSnackbar(R.string.error_message, showAction = false))
        }
    }

    @Test
    fun `onSnackbarShown - send null message`() = testCoroutineRule.runBlockingTest {
        val detailViewModel = getViewModel()

        detailViewModel.onSnackbarShown()

        detailViewModel.uiState.test {
            assertThat(awaitItem()).isEqualTo(DetailUiState.ShowSnackbar(messageId = null, showAction = false))
        }
    }

    private fun getViewModel(): DetailViewModel {
        return DetailViewModel(
            savedStateHandle,
            getFavoriteCharacterByIdUseCase,
            toggleFavoriteCharacterUseCase
        )
    }
}