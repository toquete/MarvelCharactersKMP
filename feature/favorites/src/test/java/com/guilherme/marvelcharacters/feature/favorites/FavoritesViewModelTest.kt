package com.guilherme.marvelcharacters.feature.favorites

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.guilherme.marvelcharacters.core.testing.util.BaseUnitTest
import com.guilherme.marvelcharacters.domain.usecase.DeleteAllFavoriteCharactersUseCase
import com.guilherme.marvelcharacters.domain.usecase.GetFavoriteCharactersUseCase
import com.guilherme.marvelcharacters.feature.favorites.util.Fixtures
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
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
    fun `onDeleteAllClick - send success message`() = runTest {
        favoritesViewModel.onDeleteAllClick()

        coVerify { deleteAllFavoriteCharactersUseCase() }

        favoritesViewModel.uiState.test {
            assertThat(awaitItem()).isEqualTo(FavoritesUiState.ShowSnackbar(R.string.character_deleted))
        }
    }

    @Test
    fun `onDeleteAllClick - send error message`() = runTest {
        coEvery { deleteAllFavoriteCharactersUseCase() } throws IOException()

        favoritesViewModel.onDeleteAllClick()

        favoritesViewModel.uiState.test {
            assertThat(awaitItem()).isEqualTo(FavoritesUiState.ShowSnackbar(R.string.error_message))
        }
    }

    @Test
    fun `onSnackbarShown - send null message`() = runTest {
        favoritesViewModel.onSnackbarShown()

        favoritesViewModel.uiState.test {
            assertThat(awaitItem()).isEqualTo(FavoritesUiState.ShowSnackbar(messageId = null))
        }
    }

    @Test
    fun `init - send favorites list`() = runTest {
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