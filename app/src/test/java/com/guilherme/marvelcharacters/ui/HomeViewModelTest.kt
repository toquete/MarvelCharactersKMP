package com.guilherme.marvelcharacters.ui

import androidx.lifecycle.Observer
import com.google.common.truth.Truth.assertThat
import com.guilherme.marvelcharacters.R
import com.guilherme.marvelcharacters.data.repository.PreferenceRepository
import com.guilherme.marvelcharacters.domain.model.Character
import com.guilherme.marvelcharacters.domain.model.Image
import com.guilherme.marvelcharacters.domain.repository.CharacterRepository
import com.guilherme.marvelcharacters.infrastructure.BaseUnitTest
import com.guilherme.marvelcharacters.ui.home.HomeViewModel
import com.guilherme.marvelcharacters.util.getOrAwaitValue
import com.guilherme.marvelcharacters.util.observeForTesting
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verifySequence
import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.ResponseBody
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

@ExperimentalCoroutinesApi
class HomeViewModelTest : BaseUnitTest() {

    private lateinit var homeViewModel: HomeViewModel

    @RelaxedMockK
    private lateinit var characterRepository: CharacterRepository

    @RelaxedMockK
    private lateinit var preferenceRepository: PreferenceRepository

    @RelaxedMockK
    private lateinit var observer: Observer<HomeViewModel.CharacterListState>

    override fun setUp() {
        super.setUp()
        homeViewModel = HomeViewModel(characterRepository, preferenceRepository)
    }

    @Test
    fun `onSearchCharacter - send success state when list is loaded`() {
        val character = Character(0, "Spider-Man", "The Amazing Spider-Man", Image("", ""))
        val characterList = listOf(character)

        coEvery { characterRepository.getCharacters(any()) } returns characterList

        homeViewModel.states.observeForTesting(observer) {
            homeViewModel.onSearchCharacter("spider")

            verifySequence {
                observer.onChanged(HomeViewModel.CharacterListState.Loading)
                observer.onChanged(HomeViewModel.CharacterListState.Characters(characterList))
            }
        }
    }

    @Test
    fun `onSearchCharacter - send empty state when list is empty`() {
        val characterList = emptyList<Character>()

        coEvery { characterRepository.getCharacters(any()) } returns characterList

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
        coEvery { characterRepository.getCharacters(any()) } throws HttpException(
            Response.error<String>(
                404,
                ResponseBody.create(null, "xablau")
            )
        )

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
        coEvery { characterRepository.getCharacters(any()) } throws IOException()

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
        val character = Character(0, "Spider-Man", "The Amazing Spider-Man", Image("", ""))

        homeViewModel.navigateToDetail.observeForTesting {
            homeViewModel.onItemClick(character)

            assertThat(homeViewModel.navigateToDetail.getOrAwaitValue().peekContent()).isEqualTo(character)
        }
    }

    @Test
    fun `onActionItemClick - change theme`() {
        preferenceRepository.isDarkTheme = true

        homeViewModel.onActionItemClick()

        assertThat(preferenceRepository.isDarkTheme).isFalse()
    }
}