package com.guilherme.marvelcharacters.ui

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.guilherme.marvelcharacters.R
import com.guilherme.marvelcharacters.domain.model.Character
import com.guilherme.marvelcharacters.domain.model.Image
import com.guilherme.marvelcharacters.domain.usecase.GetCharactersUseCase
import com.guilherme.marvelcharacters.infrastructure.BaseUnitTest
import com.guilherme.marvelcharacters.mapper.CharacterMapper
import com.guilherme.marvelcharacters.model.CharacterVO
import com.guilherme.marvelcharacters.model.ImageVO
import com.guilherme.marvelcharacters.ui.home.HomeEvent
import com.guilherme.marvelcharacters.ui.home.HomeState
import com.guilherme.marvelcharacters.ui.home.HomeViewModel
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import okhttp3.ResponseBody
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

@ExperimentalCoroutinesApi
class HomeViewModelTest : BaseUnitTest() {

    private lateinit var homeViewModel: HomeViewModel

    @RelaxedMockK
    private lateinit var getCharactersUseCase: GetCharactersUseCase

    override fun setUp() {
        super.setUp()
        homeViewModel = HomeViewModel(
            getCharactersUseCase,
            mapper = CharacterMapper()
        )
    }

    @Test
    fun `onSearchCharacter - send success state when list is loaded`() = testCoroutineRule.runBlockingTest {
        val character = Character(0, "Spider-Man", "The Amazing Spider-Man", Image("", ""))
        val characterVO = CharacterVO(0, "Spider-Man", "The Amazing Spider-Man", ImageVO("", ""))
        val characterList = listOf(character)
        val successState = HomeState(
            isLoading = false,
            characters = listOf(characterVO),
            errorMessageId = null
        )

        coEvery { getCharactersUseCase(any(), any(), any()) } returns characterList

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
        coEvery { getCharactersUseCase(any(), any(), any()) } throws HttpException(
            Response.error<String>(
                404,
                ResponseBody.create(null, "xablau")
            )
        )

        homeViewModel.state.test {
            homeViewModel.onSearchCharacter("spider")

            assertThat(awaitItem()).isEqualTo(HomeState.initialState())
            assertThat(awaitItem()).isEqualTo(errorState.copy(isLoading = true, errorMessageId = null))
            assertThat(awaitItem()).isEqualTo(errorState)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onSearchCharacter - send internet error state`() = testCoroutineRule.runBlockingTest {
        val errorState = HomeState(
            isLoading = false,
            characters = emptyList(),
            errorMessageId = R.string.network_error_message
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
        val character = CharacterVO(0, "Spider-Man", "The Amazing Spider-Man", ImageVO("", ""))

        homeViewModel.onItemClick(character)

        homeViewModel.event.test {
            assertThat(awaitItem()).isEqualTo(HomeEvent.NavigateToDetails(character))
        }
    }
}