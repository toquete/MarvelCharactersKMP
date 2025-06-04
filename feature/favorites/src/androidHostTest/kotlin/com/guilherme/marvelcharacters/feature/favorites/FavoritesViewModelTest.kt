package com.guilherme.marvelcharacters.feature.favorites

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.guilherme.marvelcharacters.core.testing.util.BaseUnitTest
import com.guilherme.marvelcharacters.core.ui.Resources
import com.guilherme.marvelcharacters.core.ui.SnackbarMessageMP
import com.guilherme.marvelcharacters.core.ui.UiTextMP
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
import org.junit.Before
import org.junit.Test
import java.io.IOException

@ExperimentalCoroutinesApi
class FavoritesViewModelTest : BaseUnitTest() {

    private lateinit var favoritesViewModel: FavoritesViewModel

    @RelaxedMockK
    private lateinit var getFavoriteCharactersUseCase: GetFavoriteCharactersUseCase

    @RelaxedMockK
    private lateinit var deleteAllFavoriteCharactersUseCase: DeleteAllFavoriteCharactersUseCase

    @Before
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

        favoritesViewModel.state.test {
            assertThat(awaitItem()).isEqualTo(
                FavoritesState(
                    snackbarMessage = SnackbarMessageMP(
                        text = UiTextMP.ResourceString(Resources.String.CharacterDeleted)
                    )
                )
            )
        }
    }

    @Test
    fun `onDeleteAllClick - send error message`() = runTest {
        coEvery { deleteAllFavoriteCharactersUseCase() } throws IOException()

        favoritesViewModel.onDeleteAllClick()

        favoritesViewModel.state.test {
            assertThat(awaitItem()).isEqualTo(
                FavoritesState(
                    snackbarMessage = SnackbarMessageMP(
                        text = UiTextMP.ResourceString(Resources.String.ErrorMessage)
                    )
                )
            )
        }
    }

    @Test
    fun `init - send favorites list`() = runTest {
        every { getFavoriteCharactersUseCase() } returns flowOf(Fixtures.characterList)

        val viewModel = FavoritesViewModel(
            getFavoriteCharactersUseCase,
            deleteAllFavoriteCharactersUseCase
        )

        viewModel.state.test {
            assertThat(awaitItem()).isEqualTo(FavoritesState(Fixtures.characterList))
        }
    }

    @Test
    fun `onSnackbarShown - clear messageId`() = runTest {
        favoritesViewModel.onSnackbarShown()

        favoritesViewModel.state.test {
            assertThat(awaitItem()).isEqualTo(FavoritesState(snackbarMessage = null))
        }
    }
}