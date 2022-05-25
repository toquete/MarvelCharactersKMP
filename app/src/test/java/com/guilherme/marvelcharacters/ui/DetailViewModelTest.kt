package com.guilherme.marvelcharacters.ui

import android.database.sqlite.SQLiteException
import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.guilherme.marvelcharacters.R
import com.guilherme.marvelcharacters.domain.model.Character
import com.guilherme.marvelcharacters.domain.usecase.DeleteFavoriteCharacterUseCase
import com.guilherme.marvelcharacters.domain.usecase.GetCharacterByIdUseCase
import com.guilherme.marvelcharacters.domain.usecase.InsertFavoriteCharacterUseCase
import com.guilherme.marvelcharacters.domain.usecase.IsCharacterFavoriteUseCase
import com.guilherme.marvelcharacters.infrastructure.BaseUnitTest
import com.guilherme.marvelcharacters.ui.detail.DetailComposeState
import com.guilherme.marvelcharacters.ui.detail.DetailDestination
import com.guilherme.marvelcharacters.ui.detail.DetailViewModel
import com.guilherme.marvelcharacters.ui.detail.SnackbarMessage
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
    private lateinit var savedStateHandle: SavedStateHandle

    @RelaxedMockK
    private lateinit var getCharacterByIdUseCase: GetCharacterByIdUseCase

    @RelaxedMockK
    private lateinit var isCharacterFavoriteUseCase: IsCharacterFavoriteUseCase

    @RelaxedMockK
    private lateinit var deleteFavoriteCharacterUseCase: DeleteFavoriteCharacterUseCase

    @RelaxedMockK
    private lateinit var insertFavoriteCharacterUseCase: InsertFavoriteCharacterUseCase

    private lateinit var detailViewModel: DetailViewModel

    private val characterId = 0
    private val character = Character(
        characterId,
        name = "Spider-Man",
        description = "The Amazing Spider-Man",
        thumbnail = ""
    )

    override fun setUp() {
        super.setUp()

        every { savedStateHandle.get<Int>(DetailDestination.characterIdArg) } returns characterId
        every { getCharacterByIdUseCase(characterId) } returns flowOf(character)
        every { isCharacterFavoriteUseCase(character.id) } returns flowOf(false)
    }

    @Test
    fun `init - send character state`() = testCoroutineRule.runBlockingTest {
        every { isCharacterFavoriteUseCase(character.id) } returns flowOf(true)
        setupViewModel()

        detailViewModel.state.test {
            assertThat(awaitItem()).isEqualTo(DetailComposeState(character, isFavorite = true))
            expectNoEvents()
        }
    }

    @Test
    fun `onFabClick - send deleted character event`() = testCoroutineRule.runBlockingTest {
        val expected = DetailComposeState(
            character,
            isFavorite = true,
            message = SnackbarMessage(R.string.character_deleted, showAction = true)
        )
        every { isCharacterFavoriteUseCase(character.id) } returns flowOf(true)
        setupViewModel()

        detailViewModel.onFabClick(character)

        detailViewModel.state.test {
            assertThat(awaitItem()).isEqualTo(expected)
            expectNoEvents()
        }
        coVerify { deleteFavoriteCharacterUseCase(character.id) }
    }

    @Test
    fun `onFabClick - send added character event`() = testCoroutineRule.runBlockingTest {
        val expected = DetailComposeState(
            character,
            isFavorite = false,
            message = SnackbarMessage(R.string.character_added, showAction = false)
        )
        setupViewModel()

        detailViewModel.onFabClick(character)

        detailViewModel.state.test {
            assertThat(awaitItem()).isEqualTo(expected)
            expectNoEvents()
        }
        coVerify { insertFavoriteCharacterUseCase(character) }
    }

    @Test
    fun `onFabClick - send generic error event`() = testCoroutineRule.runBlockingTest {
        val expected = DetailComposeState(
            character,
            isFavorite = false,
            message = SnackbarMessage(R.string.error_message, showAction = false)
        )
        coEvery { insertFavoriteCharacterUseCase(character) } throws SQLiteException()
        setupViewModel()

        detailViewModel.onFabClick(character)

        detailViewModel.state.test {
            assertThat(awaitItem()).isEqualTo(expected)
            expectNoEvents()
        }
        coVerify { insertFavoriteCharacterUseCase(character) }
    }

    @Test
    fun `onUndoClick - undo changes`() = testCoroutineRule.runBlockingTest {
        setupViewModel()

        detailViewModel.onUndoClick(character)

        coVerify { insertFavoriteCharacterUseCase(character) }
    }

    @Test
    fun `onUndoClick - send error message on undo changes`() = testCoroutineRule.runBlockingTest {
        val expected = DetailComposeState(
            character,
            isFavorite = false,
            message = SnackbarMessage(R.string.error_message, showAction = false)
        )
        coEvery { insertFavoriteCharacterUseCase(character) } throws SQLiteException()
        setupViewModel()

        detailViewModel.onUndoClick(character)

        detailViewModel.state.test {
            assertThat(awaitItem()).isEqualTo(expected)
            expectNoEvents()
        }
    }

    private fun setupViewModel() {
        detailViewModel = DetailViewModel(
            savedStateHandle,
            getCharacterByIdUseCase,
            isCharacterFavoriteUseCase,
            deleteFavoriteCharacterUseCase,
            insertFavoriteCharacterUseCase
        )
    }
}