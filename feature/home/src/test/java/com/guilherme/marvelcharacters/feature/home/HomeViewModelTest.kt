package com.guilherme.marvelcharacters.feature.home

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.guilherme.marvelcharacters.core.testing.util.BaseUnitTest
import com.guilherme.marvelcharacters.domain.usecase.GetCharactersUseCase
import com.guilherme.marvelcharacters.feature.home.util.Fixtures
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
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
    fun `onSearchCharacter - send success state when list is loaded`() = runTest {
        coEvery { getCharactersUseCase(any()) } returns Fixtures.characterList

        homeViewModel.state.test {
            homeViewModel.onSearchCharacter("spider")

            assertThat(awaitItem()).isEqualTo(HomeState())
            assertThat(awaitItem()).isEqualTo(HomeState(isLoading = true))
            assertThat(awaitItem()).isEqualTo(HomeState(characters = Fixtures.characterList, isLoading = false))
        }
    }

    @Test
    fun `onSearchCharacter - send empty state when list is empty`() = runTest {
        coEvery { getCharactersUseCase(any()) } returns emptyList()

        homeViewModel.state.test {
            homeViewModel.onSearchCharacter("spider")

            assertThat(awaitItem()).isEqualTo(HomeState())
            assertThat(awaitItem()).isEqualTo(HomeState(isLoading = true))
            assertThat(awaitItem()).isEqualTo(HomeState(errorMessageId = R.string.empty_state_message, isLoading = false))
        }
    }

    @Test
    fun `onSearchCharacter - send error state on request error`() = runTest {
        coEvery { getCharactersUseCase(any()) } throws IOException()

        homeViewModel.state.test {
            homeViewModel.onSearchCharacter("spider")

            assertThat(awaitItem()).isEqualTo(HomeState())
            assertThat(awaitItem()).isEqualTo(HomeState(isLoading = true))
            assertThat(awaitItem()).isEqualTo(HomeState(errorMessageId = R.string.request_error_message, isLoading = false))
        }
    }
}