package com.guilherme.marvelcharacters.ui

import androidx.lifecycle.Observer
import com.google.common.truth.Truth.assertThat
import com.guilherme.marvelcharacters.R
import com.guilherme.marvelcharacters.domain.model.Character
import com.guilherme.marvelcharacters.domain.model.Image
import com.guilherme.marvelcharacters.domain.usecase.GetCharactersUseCase
import com.guilherme.marvelcharacters.domain.usecase.GetDarkModeUseCase
import com.guilherme.marvelcharacters.domain.usecase.IsDarkModeEnabledUseCase
import com.guilherme.marvelcharacters.domain.usecase.ToggleDarkModeUseCase
import com.guilherme.marvelcharacters.infrastructure.BaseUnitTest
import com.guilherme.marvelcharacters.ui.home.HomeViewModel
import com.guilherme.marvelcharacters.ui.model.CharacterVO
import com.guilherme.marvelcharacters.ui.model.ImageVO
import com.guilherme.marvelcharacters.util.getOrAwaitValue
import com.guilherme.marvelcharacters.util.observeForTesting
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verifyOrder
import io.mockk.verifySequence
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
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

    @RelaxedMockK
    private lateinit var getDarkModeUseCase: GetDarkModeUseCase

    @RelaxedMockK
    private lateinit var toggleDarkModeUseCase: ToggleDarkModeUseCase

    @RelaxedMockK
    private lateinit var isDarkModeEnabledUseCase: IsDarkModeEnabledUseCase

    @RelaxedMockK
    private lateinit var stateObserver: Observer<HomeViewModel.CharacterListState>

    override fun setUp() {
        super.setUp()
        homeViewModel = HomeViewModel(
            getCharactersUseCase,
            getDarkModeUseCase,
            toggleDarkModeUseCase,
            isDarkModeEnabledUseCase,
            dispatcher = testCoroutineRule.testCoroutineDispatcher
        )
    }

    @Test
    fun `onSearchCharacter - send success state when list is loaded`() {
        val character = Character(0, "Spider-Man", "The Amazing Spider-Man", Image("", ""))
        val characterVO = CharacterVO(0, "Spider-Man", "The Amazing Spider-Man", ImageVO("", ""))
        val characterList = listOf(character)

        coEvery { getCharactersUseCase(any(), any(), any()) } returns flowOf(characterList)

        homeViewModel.states.observeForTesting(stateObserver) {
            homeViewModel.onSearchCharacter("spider")

            verifySequence {
                stateObserver.onChanged(HomeViewModel.CharacterListState.Loading)
                stateObserver.onChanged(HomeViewModel.CharacterListState.Characters(listOf(characterVO)))
            }
        }
    }

    @Test
    fun `onSearchCharacter - send empty state when list is empty`() {
        val characterList = emptyList<Character>()

        coEvery { getCharactersUseCase(any(), any(), any()) } returns flowOf(characterList)

        homeViewModel.states.observeForTesting(stateObserver) {
            homeViewModel.onSearchCharacter("spider")

            verifySequence {
                stateObserver.onChanged(HomeViewModel.CharacterListState.Loading)
                stateObserver.onChanged(HomeViewModel.CharacterListState.EmptyState)
            }
        }
    }

    @Test
    fun `onSearchCharacter - send error state on request error`() {
        coEvery { getCharactersUseCase(any(), any(), any()) } returns flow {
            throw HttpException(
                Response.error<String>(
                    404,
                    ResponseBody.create(null, "xablau")
                )
            )
        }

        homeViewModel.states.observeForTesting(stateObserver) {
            homeViewModel.onSearchCharacter("spider")

            verifySequence {
                stateObserver.onChanged(HomeViewModel.CharacterListState.Loading)
                stateObserver.onChanged(HomeViewModel.CharacterListState.ErrorState(R.string.request_error_message))
            }
        }
    }

    @Test
    fun `onSearchCharacter - send internet error state`() {
        coEvery { getCharactersUseCase(any(), any(), any()) } returns flow { throw IOException() }

        homeViewModel.states.observeForTesting(stateObserver) {
            homeViewModel.onSearchCharacter("spider")

            verifySequence {
                stateObserver.onChanged(HomeViewModel.CharacterListState.Loading)
                stateObserver.onChanged(HomeViewModel.CharacterListState.ErrorState(R.string.network_error_message))
            }
        }
    }

    @Test
    fun `onItemClick - send character to details screen`() {
        val character = CharacterVO(0, "Spider-Man", "The Amazing Spider-Man", ImageVO("", ""))

        homeViewModel.navigateToDetail.observeForTesting {
            homeViewModel.onItemClick(character)

            assertThat(
                homeViewModel.navigateToDetail.getOrAwaitValue().peekContent()
            ).isEqualTo(character)
        }
    }

    @Test
    fun `onActionItemClick - change theme`() {
        every { isDarkModeEnabledUseCase() } returns true

        homeViewModel.onActionItemClick()

        verifyOrder {
            isDarkModeEnabledUseCase()
            toggleDarkModeUseCase(isEnabled = false)
            getDarkModeUseCase()
        }
    }
}