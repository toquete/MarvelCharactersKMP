package com.guilherme.marvelcharacters.feature.home

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.guilherme.marvelcharacters.core.testing.util.BaseUnitTest
import com.guilherme.marvelcharacters.domain.usecase.GetCharactersUseCase
import com.guilherme.marvelcharacters.feature.home.util.Fixtures
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
        coEvery { getCharactersUseCase(any(), any(), any()) } returns Fixtures.characterList

        homeViewModel.uiState.test {
            homeViewModel.onSearchCharacter("spider")

            assertThat(awaitItem()).isEqualTo(HomeUiState.Empty)
            assertThat(awaitItem()).isEqualTo(HomeUiState.Loading)
            assertThat(awaitItem()).isEqualTo(HomeUiState.Success(Fixtures.characterList))
        }
    }

    @Test
    fun `onSearchCharacter - send empty state when list is empty`() = testCoroutineRule.runBlockingTest {
        coEvery { getCharactersUseCase(any(), any(), any()) } returns emptyList()

        homeViewModel.uiState.test {
            homeViewModel.onSearchCharacter("spider")

            assertThat(awaitItem()).isEqualTo(HomeUiState.Empty)
            assertThat(awaitItem()).isEqualTo(HomeUiState.Loading)
            assertThat(awaitItem()).isEqualTo(HomeUiState.Error(R.string.empty_state_message))
        }
    }

    @Test
    fun `onSearchCharacter - send error state on request error`() = testCoroutineRule.runBlockingTest {
        coEvery { getCharactersUseCase(any(), any(), any()) } throws IOException()

        homeViewModel.uiState.test {
            homeViewModel.onSearchCharacter("spider")

            assertThat(awaitItem()).isEqualTo(HomeUiState.Empty)
            assertThat(awaitItem()).isEqualTo(HomeUiState.Loading)
            assertThat(awaitItem()).isEqualTo(HomeUiState.Error(R.string.request_error_message))
        }
    }
}