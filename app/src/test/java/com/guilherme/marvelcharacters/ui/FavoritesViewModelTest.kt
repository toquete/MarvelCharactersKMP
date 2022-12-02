package com.guilherme.marvelcharacters.ui

import android.database.sqlite.SQLiteException
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.guilherme.marvelcharacters.R
import com.guilherme.marvelcharacters.domain.usecase.DeleteAllFavoriteCharactersUseCase
import com.guilherme.marvelcharacters.domain.usecase.GetFavoriteCharactersUseCase
import com.guilherme.marvelcharacters.infrastructure.BaseUnitTest
import com.guilherme.marvelcharacters.ui.favorites.FavoritesEvent
import com.guilherme.marvelcharacters.ui.favorites.FavoritesState
import com.guilherme.marvelcharacters.ui.favorites.FavoritesViewModel
import com.guilherme.marvelcharacters.util.Fixtures
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import org.junit.Test

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

        favoritesViewModel.event.test {
            assertThat(awaitItem()).isEqualTo(FavoritesEvent.ShowSnackbarMessage(R.string.character_deleted))
        }
    }

    @Test
    fun `onDeleteAllClick - send error message`() = testCoroutineRule.runBlockingTest {
        coEvery { deleteAllFavoriteCharactersUseCase() } throws SQLiteException()

        favoritesViewModel.onDeleteAllClick()

        coVerify { deleteAllFavoriteCharactersUseCase() }

        favoritesViewModel.event.test {
            assertThat(awaitItem()).isEqualTo(FavoritesEvent.ShowSnackbarMessage(R.string.error_message))
        }
    }

    @Test
    fun `onFavoriteItemClick - send character to details screen`() = testCoroutineRule.runBlockingTest {
        favoritesViewModel.onFavoriteItemClick(Fixtures.character)

        favoritesViewModel.event.test {
            assertThat(awaitItem()).isEqualTo(FavoritesEvent.NavigateToDetail(Fixtures.character))
        }
    }

    @Test
    fun `init - send favorites list`() = testCoroutineRule.runBlockingTest {
        every { getFavoriteCharactersUseCase() } returns flowOf(Fixtures.characterList)

        val viewModel = FavoritesViewModel(
            getFavoriteCharactersUseCase,
            deleteAllFavoriteCharactersUseCase
        )

        viewModel.state.test {
            assertThat(awaitItem()).isEqualTo(FavoritesState(Fixtures.characterList))
        }
    }
}