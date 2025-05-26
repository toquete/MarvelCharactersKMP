package com.guilherme.marvelcharacters.feature.detail

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.guilherme.marvelcharacters.core.testing.util.BaseUnitTest
import com.guilherme.marvelcharacters.domain.model.FavoriteCharacter
import com.guilherme.marvelcharacters.domain.usecase.GetFavoriteCharacterByIdUseCase
import com.guilherme.marvelcharacters.domain.usecase.ToggleFavoriteCharacterUseCase
import com.guilherme.marvelcharacters.feature.detail.navigation.DetailRoute
import com.guilherme.marvelcharacters.feature.detail.util.Fixtures
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.IOException

@ExperimentalCoroutinesApi
class DetailViewModelTest : BaseUnitTest() {

    @RelaxedMockK
    private lateinit var getFavoriteCharacterByIdUseCase: GetFavoriteCharacterByIdUseCase

    @RelaxedMockK
    private lateinit var toggleFavoriteCharacterUseCase: ToggleFavoriteCharacterUseCase

    @RelaxedMockK
    private lateinit var savedStateHandle: SavedStateHandle

    @Before
    override fun setUp() {
        super.setUp()
        mockkStatic("androidx.navigation.SavedStateHandleKt")
        every { savedStateHandle.toRoute<Any>(any(), any()) } returns DetailRoute(id = Fixtures.character.id)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `init - send character state`() = runTest {
        val favoriteCharacter = FavoriteCharacter(Fixtures.character, isFavorite = true)
        every { getFavoriteCharacterByIdUseCase(Fixtures.character.id) } returns flowOf(favoriteCharacter)

        val detailViewModel = getViewModel()

        detailViewModel.state.test {
            assertThat(awaitItem()).isEqualTo(DetailState(favoriteCharacter))
        }
    }

    @Test
    fun `onFabClick - send deleted character event`() = runTest {
        val favoriteCharacter = FavoriteCharacter(Fixtures.character, isFavorite = true)
        every { getFavoriteCharacterByIdUseCase(Fixtures.character.id) } returns flowOf(favoriteCharacter)

        val detailViewModel = getViewModel()

        // simulate first collection
        val job = detailViewModel.state.launchIn(this)

        detailViewModel.onFabClick(isFavorite = true)

        coVerify { toggleFavoriteCharacterUseCase(Fixtures.character.id, isFavorite = true) }

        detailViewModel.state.test {
            assertThat(awaitItem()).isEqualTo(DetailState(favoriteCharacter, R.string.character_deleted, showAction = true))
            job.cancel()
        }
    }

    @Test
    fun `onFabClick - send added character event`() = runTest {
        val favoriteCharacter = FavoriteCharacter(Fixtures.character, isFavorite = false)
        every { getFavoriteCharacterByIdUseCase(Fixtures.character.id) } returns flowOf(favoriteCharacter)

        val detailViewModel = getViewModel()

        // simulate first collection
        val job = detailViewModel.state.launchIn(this)

        detailViewModel.onFabClick(isFavorite = false)

        coVerify { toggleFavoriteCharacterUseCase(Fixtures.character.id, isFavorite = false) }

        detailViewModel.state.test {
            assertThat(awaitItem()).isEqualTo(DetailState(favoriteCharacter, R.string.character_added, showAction = false))
            job.cancel()
        }
    }

    @Test
    fun `onFabClick - send generic error event`() = runTest {
        val favoriteCharacter = FavoriteCharacter(Fixtures.character, isFavorite = false)
        every { getFavoriteCharacterByIdUseCase(Fixtures.character.id) } returns flowOf(favoriteCharacter)
        coEvery { toggleFavoriteCharacterUseCase(Fixtures.character.id, isFavorite = false) } throws IOException()

        val detailViewModel = getViewModel()

        detailViewModel.onFabClick(isFavorite = false)

        coVerify { toggleFavoriteCharacterUseCase(Fixtures.character.id, isFavorite = false) }

        detailViewModel.state.test {
            assertThat(awaitItem()).isEqualTo(DetailState(favoriteCharacter, R.string.error_message, showAction = false))
        }
    }

    @Test
    fun `onUndoClick - undo changes`() = runTest {
        val detailViewModel = getViewModel()

        detailViewModel.onUndoClick()

        coVerify { toggleFavoriteCharacterUseCase(Fixtures.character.id, isFavorite = false) }
    }

    @Test
    fun `onUndoClick - send error message on undo changes`() = runTest {
        coEvery { toggleFavoriteCharacterUseCase(Fixtures.character.id, isFavorite = false) } throws IOException()

        val detailViewModel = getViewModel()

        detailViewModel.onUndoClick()

        detailViewModel.state.test {
            assertThat(awaitItem()).isEqualTo(DetailState(messageId = R.string.error_message, showAction = false))
        }
    }

    @Test
    fun `onSnackbarShown - send null message`() = runTest {
        val detailViewModel = getViewModel()

        detailViewModel.onSnackbarShown()

        detailViewModel.state.test {
            assertThat(awaitItem()).isEqualTo(DetailState())
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