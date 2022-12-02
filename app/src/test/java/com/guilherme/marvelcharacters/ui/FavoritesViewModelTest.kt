package com.guilherme.marvelcharacters.ui

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.guilherme.marvelcharacters.R
import com.guilherme.marvelcharacters.domain.usecase.DeleteAllFavoriteCharactersUseCase
import com.guilherme.marvelcharacters.domain.usecase.GetFavoriteCharactersUseCase
import com.guilherme.marvelcharacters.infrastructure.BaseUnitTest
import com.guilherme.marvelcharacters.ui.favorites.FavoritesUiState
import com.guilherme.marvelcharacters.ui.favorites.FavoritesViewModel
import com.guilherme.marvelcharacters.util.Fixtures
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import org.junit.Test
import java.io.IOException

@ExperimentalCoroutinesApi
class FavoritesViewModelTest : BaseUnitTest() {

    private lateinit var favoritesViewModel: FavoritesViewModel

    @RelaxedMockK
    private lateinit var getFavoriteCharactersUseCase: GetFavoriteCharactersUseCase

    @RelaxedMockK
    private lateinit var deleteAllFavoriteCharactersUseCase: DeleteAllFavoriteCharactersUseCase

    override fun setUp() {
        super.setUp()
        favoritesViewModel = FavoritesViewModel(
            getFavoriteCharactersUseCase,
            deleteAllFavoriteCharactersUseCase
        )
    }

    @Test
    fun `onDeleteAllClick - send success message`() = testCoroutineRule.runBlockingTest {
        favoritesViewModel.onDeleteAllClick()

        coVerify { deleteAllFavoriteCharactersUseCase() }

        favoritesViewModel.uiState.test {
            assertThat(awaitItem()).isEqualTo(FavoritesUiState.ShowSnackbar(R.string.character_deleted))
        }
    }

    @Test
    fun `onDeleteAllClick - send error message`() = testCoroutineRule.runBlockingTest {
        coEvery { deleteAllFavoriteCharactersUseCase() } throws IOException()

        favoritesViewModel.onDeleteAllClick()

        favoritesViewModel.uiState.test {
            assertThat(awaitItem()).isEqualTo(FavoritesUiState.ShowSnackbar(R.string.error_message))
        }
    }

    @Test
    fun `onSnackbarShown - send null message`() = testCoroutineRule.runBlockingTest {
        favoritesViewModel.onSnackbarShown()

        favoritesViewModel.uiState.test {
            assertThat(awaitItem()).isEqualTo(FavoritesUiState.ShowSnackbar(messageId = null))
        }
    }

    @Test
    fun `init - send favorites list`() = testCoroutineRule.runBlockingTest {
        every { getFavoriteCharactersUseCase() } returns flowOf(Fixtures.characterList)

        val viewModel = FavoritesViewModel(
            getFavoriteCharactersUseCase,
            deleteAllFavoriteCharactersUseCase
        )

        viewModel.uiState.test {
            assertThat(awaitItem()).isEqualTo(FavoritesUiState.Success(Fixtures.characterList))
        }
    }
}