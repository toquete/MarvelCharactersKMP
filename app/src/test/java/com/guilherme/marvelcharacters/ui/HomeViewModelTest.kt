package com.guilherme.marvelcharacters.ui

import androidx.lifecycle.Observer
import com.google.common.truth.Truth.assertThat
import com.guilherme.marvelcharacters.R
import com.guilherme.marvelcharacters.data.repository.NightModeRepositoryImpl
import com.guilherme.marvelcharacters.domain.model.Character
import com.guilherme.marvelcharacters.domain.model.Image
import com.guilherme.marvelcharacters.domain.usecase.GetCharactersUseCase
import com.guilherme.marvelcharacters.infrastructure.BaseUnitTest
import com.guilherme.marvelcharacters.ui.home.HomeViewModel
import com.guilherme.marvelcharacters.ui.model.CharacterVO
import com.guilherme.marvelcharacters.ui.model.ImageVO
import com.guilherme.marvelcharacters.util.getOrAwaitValue
import com.guilherme.marvelcharacters.util.observeForTesting
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
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
    private lateinit var preferenceRepository: NightModeRepositoryImpl

    @RelaxedMockK
    private lateinit var observer: Observer<HomeViewModel.CharacterListState>

    override fun setUp() {
        super.setUp()
        homeViewModel = HomeViewModel(
            preferenceRepository,
            getCharactersUseCase,
            dispatcher = testCoroutineRule.testCoroutineDispatcher
        )
    }

    @Test
    fun `onSearchCharacter - send success state when list is loaded`() {
        val character = Character(0, "Spider-Man", "The Amazing Spider-Man", Image("", ""))
        val characterVO = CharacterVO(0, "Spider-Man", "The Amazing Spider-Man", ImageVO("", ""))
        val characterList = listOf(character)

        coEvery { getCharactersUseCase(any()) } returns flowOf(characterList)

        homeViewModel.states.observeForTesting(observer) {
            homeViewModel.onSearchCharacter("spider")

            verifySequence {
                observer.onChanged(HomeViewModel.CharacterListState.Loading)
                observer.onChanged(HomeViewModel.CharacterListState.Characters(listOf(characterVO)))
            }
        }
    }

    @Test
    fun `onSearchCharacter - send empty state when list is empty`() {
        val characterList = emptyList<Character>()

        coEvery { getCharactersUseCase(any()) } returns flowOf(characterList)

        homeViewModel.states.observeForTesting(observer) {
            homeViewModel.onSearchCharacter("spider")

            verifySequence {
                observer.onChanged(HomeViewModel.CharacterListState.Loading)
                observer.onChanged(HomeViewModel.CharacterListState.EmptyState)
            }
        }
    }

    @Test
    fun `onSearchCharacter - send error state on request error`() {
        coEvery { getCharactersUseCase(any()) } returns flow {
            throw HttpException(
                Response.error<String>(
                    404,
                    ResponseBody.create(null, "xablau")
                )
            )
        }

        homeViewModel.states.observeForTesting(observer) {
            homeViewModel.onSearchCharacter("spider")

            verifySequence {
                observer.onChanged(HomeViewModel.CharacterListState.Loading)
                observer.onChanged(HomeViewModel.CharacterListState.ErrorState(R.string.request_error_message))
            }
        }
    }

    @Test
    fun `onSearchCharacter - send internet error state`() {
        coEvery { getCharactersUseCase(any()) } returns flow { throw IOException() }

        homeViewModel.states.observeForTesting(observer) {
            homeViewModel.onSearchCharacter("spider")

            verifySequence {
                observer.onChanged(HomeViewModel.CharacterListState.Loading)
                observer.onChanged(HomeViewModel.CharacterListState.ErrorState(R.string.network_error_message))
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
        preferenceRepository.isDarkTheme = true

        homeViewModel.onActionItemClick()

        assertThat(preferenceRepository.isDarkTheme).isFalse()
    }
}