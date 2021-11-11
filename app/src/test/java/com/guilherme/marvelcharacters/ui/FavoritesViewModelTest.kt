package com.guilherme.marvelcharacters.ui

import android.database.sqlite.SQLiteException
import com.google.common.truth.Truth.assertThat
import com.guilherme.marvelcharacters.R
import com.guilherme.marvelcharacters.domain.model.Character
import com.guilherme.marvelcharacters.domain.model.Image
import com.guilherme.marvelcharacters.domain.usecase.DeleteAllFavoriteCharactersUseCase
import com.guilherme.marvelcharacters.domain.usecase.DeleteFavoriteCharacterUseCase
import com.guilherme.marvelcharacters.domain.usecase.GetFavoriteCharactersUseCase
import com.guilherme.marvelcharacters.infrastructure.BaseUnitTest
import com.guilherme.marvelcharacters.ui.favorites.FavoritesViewModel
import com.guilherme.marvelcharacters.ui.mapper.CharacterMapper
import com.guilherme.marvelcharacters.ui.model.CharacterVO
import com.guilherme.marvelcharacters.ui.model.ImageVO
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
class FavoritesViewModelTest : BaseUnitTest() {

    private lateinit var favoritesViewModel: FavoritesViewModel

    @RelaxedMockK
    private lateinit var getFavoriteCharactersUseCase: GetFavoriteCharactersUseCase

    @RelaxedMockK
    private lateinit var deleteFavoriteCharacterUseCase: DeleteFavoriteCharacterUseCase

    @RelaxedMockK
    private lateinit var deleteAllFavoriteCharactersUseCase: DeleteAllFavoriteCharactersUseCase

    override fun setUp() {
        super.setUp()
        favoritesViewModel = FavoritesViewModel(
            getFavoriteCharactersUseCase,
            deleteFavoriteCharacterUseCase,
            deleteAllFavoriteCharactersUseCase,
            CharacterMapper(),
            testCoroutineRule.testCoroutineDispatcher
        )
    }

    @Test
    fun `deleteCharacter - check if repository was called`() {
        val characterVO = CharacterVO(0, "Spider-Man", "The Amazing Spider-Man", ImageVO("", ""))
        val character = Character(0, "Spider-Man", "The Amazing Spider-Man", Image("", ""))

        favoritesViewModel.deleteCharacter(characterVO)

        coVerify { deleteFavoriteCharacterUseCase(character) }
    }

    @Test
    fun `onDeleteAllClick - send success message`() {
        favoritesViewModel.snackbarMessage.observeForTesting {
            favoritesViewModel.onDeleteAllClick()

            coVerify { deleteAllFavoriteCharactersUseCase() }
            assertThat(
                favoritesViewModel.snackbarMessage.getOrAwaitValue().peekContent()
            ).isEqualTo(R.string.character_deleted)
        }
    }

    @Test
    fun `onDeleteAllClick - send error message`() {
        coEvery { deleteAllFavoriteCharactersUseCase() } throws SQLiteException()

        favoritesViewModel.snackbarMessage.observeForTesting {
            favoritesViewModel.onDeleteAllClick()

            coVerify { deleteAllFavoriteCharactersUseCase() }
            assertThat(
                favoritesViewModel.snackbarMessage.getOrAwaitValue().peekContent()
            ).isEqualTo(R.string.error_message)
        }
    }

    @Test
    fun `onFavoriteItemClick - send character to details screen`() {
        val character = CharacterVO(0, "Spider-Man", "The Amazing Spider-Man", ImageVO("", ""))

        favoritesViewModel.navigateToDetail.observeForTesting {
            favoritesViewModel.onFavoriteItemClick(character)

            assertThat(
                favoritesViewModel.navigateToDetail.getOrAwaitValue().peekContent()
            ).isEqualTo(character)
        }
    }

    @Test
    fun `init - send favorites list`() {
        val character = Character(0, "Spider-Man", "The Amazing Spider-Man", Image("", ""))
        val characterVO = CharacterVO(0, "Spider-Man", "The Amazing Spider-Man", ImageVO("", ""))

        every { getFavoriteCharactersUseCase() } returns flowOf(listOf(character))

        val viewModel = FavoritesViewModel(
            getFavoriteCharactersUseCase,
            deleteFavoriteCharacterUseCase,
            deleteAllFavoriteCharactersUseCase,
            CharacterMapper(),
            testCoroutineRule.testCoroutineDispatcher
        )

        viewModel.list.observeForTesting {
            assertThat(viewModel.list.getOrAwaitValue()).isEqualTo(listOf(characterVO))
        }
    }
}