package com.guilherme.marvelcharacters.ui

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.guilherme.marvelcharacters.R
import com.guilherme.marvelcharacters.domain.model.Character
import com.guilherme.marvelcharacters.domain.model.Image
import com.guilherme.marvelcharacters.domain.usecase.GetCharactersUseCase
import com.guilherme.marvelcharacters.infrastructure.BaseUnitTest
import com.guilherme.marvelcharacters.ui.home.HomeViewModel
import com.guilherme.marvelcharacters.ui.model.CharacterVO
import com.guilherme.marvelcharacters.ui.model.ImageVO
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
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
            dispatcher = testCoroutineRule.testCoroutineDispatcher
        )
    }

    @Test
    fun `onSearchCharacter - send success state when list is loaded`() = testCoroutineRule.runBlockingTest {
        val character = Character(0, "Spider-Man", "The Amazing Spider-Man", Image("", ""))
        val characterVO = CharacterVO(0, "Spider-Man", "The Amazing Spider-Man", ImageVO("", ""))
        val characterList = listOf(character)
        val successState = HomeViewModel.State(
            isLoading = true,
            characters = listOf(characterVO),
            errorMessageId = null
        )

        coEvery { getCharactersUseCase(any(), any(), any()) } returns flowOf(characterList)

        homeViewModel.states.test {
            homeViewModel.onSearchCharacter("spider")

            assertThat(awaitItem()).isEqualTo(HomeViewModel.State.initialState())
            assertThat(awaitItem()).isEqualTo(successState.copy(characters = listOf()))
            assertThat(awaitItem()).isEqualTo(successState)
            assertThat(awaitItem()).isEqualTo(successState.copy(isLoading = false))
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onSearchCharacter - send empty state when list is empty`() = testCoroutineRule.runBlockingTest {
        val characterList = emptyList<Character>()
        val emptyState = HomeViewModel.State(
            isLoading = true,
            characters = emptyList(),
            errorMessageId = R.string.empty_state_message
        )

        coEvery { getCharactersUseCase(any(), any(), any()) } returns flowOf(characterList)

        homeViewModel.states.test {
            homeViewModel.onSearchCharacter("spider")

            assertThat(awaitItem()).isEqualTo(HomeViewModel.State.initialState())
            assertThat(awaitItem()).isEqualTo(emptyState.copy(errorMessageId = null))
            assertThat(awaitItem()).isEqualTo(emptyState)
            assertThat(awaitItem()).isEqualTo(emptyState.copy(isLoading = false))
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onSearchCharacter - send error state on request error`() = testCoroutineRule.runBlockingTest {
        val errorState = HomeViewModel.State(
            isLoading = false,
            characters = emptyList(),
            errorMessageId = R.string.request_error_message
        )
        coEvery { getCharactersUseCase(any(), any(), any()) } returns flow {
            throw HttpException(
                Response.error<String>(
                    404,
                    ResponseBody.create(null, "xablau")
                )
            )
        }

        homeViewModel.states.test {
            homeViewModel.onSearchCharacter("spider")

            assertThat(awaitItem()).isEqualTo(HomeViewModel.State.initialState())
            assertThat(awaitItem()).isEqualTo(errorState.copy(isLoading = true, errorMessageId = null))
            assertThat(awaitItem()).isEqualTo(errorState.copy(isLoading = false, errorMessageId = null))
            assertThat(awaitItem()).isEqualTo(errorState)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onSearchCharacter - send internet error state`() = testCoroutineRule.runBlockingTest {
        val errorState = HomeViewModel.State(
            isLoading = false,
            characters = emptyList(),
            errorMessageId = R.string.network_error_message
        )
        coEvery { getCharactersUseCase(any(), any(), any()) } returns flow { throw IOException() }

        homeViewModel.states.test {
            homeViewModel.onSearchCharacter("spider")

            assertThat(awaitItem()).isEqualTo(HomeViewModel.State.initialState())
            assertThat(awaitItem()).isEqualTo(errorState.copy(isLoading = true, errorMessageId = null))
            assertThat(awaitItem()).isEqualTo(errorState.copy(isLoading = false, errorMessageId = null))
            assertThat(awaitItem()).isEqualTo(errorState)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onItemClick - send character to details screen`() = runBlockingTest {
        val character = CharacterVO(0, "Spider-Man", "The Amazing Spider-Man", ImageVO("", ""))

        homeViewModel.onItemClick(character)

        homeViewModel.events.test {
            assertThat(awaitItem()).isEqualTo(HomeViewModel.Event.NavigateToDetails(character))
        }
    }
}