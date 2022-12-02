package com.guilherme.marvelcharacters.ui

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.guilherme.marvelcharacters.R
import com.guilherme.marvelcharacters.core.model.Character
import com.guilherme.marvelcharacters.domain.usecase.GetCharactersUseCase
import com.guilherme.marvelcharacters.infrastructure.BaseUnitTest
import com.guilherme.marvelcharacters.ui.home.HomeEvent
import com.guilherme.marvelcharacters.ui.home.HomeState
import com.guilherme.marvelcharacters.ui.home.HomeViewModel
import com.guilherme.marvelcharacters.util.Fixtures
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import java.io.IOException

@ExperimentalCoroutinesApi
class HomeViewModelTest : BaseUnitTest() {

    private lateinit var homeViewModel: HomeViewModel

    @RelaxedMockK
    private lateinit var getCharactersUseCase: GetCharactersUseCase

    override fun setUp() {
        super.setUp()
        homeViewModel = HomeViewModel(getCharactersUseCase)
    }

    @Test
    fun `onSearchCharacter - send success state when list is loaded`() = testCoroutineRule.runBlockingTest {
        val successState = HomeState(
            isLoading = false,
            characters = Fixtures.characterList,
            errorMessageId = null
        )

        coEvery { getCharactersUseCase(any(), any(), any()) } returns Fixtures.characterList

        homeViewModel.state.test {
            homeViewModel.onSearchCharacter("spider")

            assertThat(awaitItem()).isEqualTo(HomeState.initialState())
            assertThat(awaitItem()).isEqualTo(successState.copy(isLoading = true, characters = listOf()))
            assertThat(awaitItem()).isEqualTo(successState)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onSearchCharacter - send empty state when list is empty`() = testCoroutineRule.runBlockingTest {
        val characterList = emptyList<Character>()
        val emptyState = HomeState(
            isLoading = false,
            characters = emptyList(),
            errorMessageId = R.string.empty_state_message
        )

        coEvery { getCharactersUseCase(any(), any(), any()) } returns characterList

        homeViewModel.state.test {
            homeViewModel.onSearchCharacter("spider")

            assertThat(awaitItem()).isEqualTo(HomeState.initialState())
            assertThat(awaitItem()).isEqualTo(emptyState.copy(isLoading = true, errorMessageId = null))
            assertThat(awaitItem()).isEqualTo(emptyState)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onSearchCharacter - send error state on request error`() = testCoroutineRule.runBlockingTest {
        val errorState = HomeState(
            isLoading = false,
            characters = emptyList(),
            errorMessageId = R.string.request_error_message
        )
        coEvery { getCharactersUseCase(any(), any(), any()) } throws IOException()

        homeViewModel.state.test {
            homeViewModel.onSearchCharacter("spider")

            assertThat(awaitItem()).isEqualTo(HomeState.initialState())
            assertThat(awaitItem()).isEqualTo(errorState.copy(isLoading = true, errorMessageId = null))
            assertThat(awaitItem()).isEqualTo(errorState)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onItemClick - send character to details screen`() = runBlockingTest {
        homeViewModel.onItemClick(Fixtures.character)

        homeViewModel.event.test {
            assertThat(awaitItem()).isEqualTo(HomeEvent.NavigateToDetails(Fixtures.character))
        }
    }
}