package com.guilherme.marvelcharacters.ui

import androidx.lifecycle.Observer
import com.google.common.truth.Truth.assertThat
import com.guilherme.marvelcharacters.data.model.Character
import com.guilherme.marvelcharacters.data.model.Image
import com.guilherme.marvelcharacters.data.repository.CharacterRepository
import com.guilherme.marvelcharacters.infrastructure.BaseUnitTest
import com.guilherme.marvelcharacters.ui.home.HomeViewModel
import com.guilherme.marvelcharacters.util.getOrAwaitValue
import com.guilherme.marvelcharacters.util.observeForTesting
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verifySequence
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test

@ExperimentalCoroutinesApi
class HomeViewModelTest : BaseUnitTest() {

    private lateinit var homeViewModel: HomeViewModel

    @RelaxedMockK
    private lateinit var characterRepository: CharacterRepository

    @RelaxedMockK
    private lateinit var observer: Observer<HomeViewModel.CharacterListState>

    override fun setUp() {
        super.setUp()
        homeViewModel = HomeViewModel(characterRepository, testCoroutineRule.testCoroutineDispatcher)
    }

    @Test
    fun `onSearchCharacter - envia estado de sucesso ao carregar a lista`() = testCoroutineRule.runBlockingTest {
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
    fun `onSearchCharacter - envia estado vazio ao carregar uma lista vazia`() = testCoroutineRule.runBlockingTest {
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
    fun `onSearchCharacter - envia estado de erro`() = testCoroutineRule.runBlockingTest {
        val exception = Exception("This is an error")
        coEvery { characterRepository.getCharacters(any()) } throws exception

        homeViewModel.states.observeForTesting(observer) {
            homeViewModel.onSearchCharacter("spider")

            verifySequence {
                observer.onChanged(HomeViewModel.CharacterListState.Loading)
                observer.onChanged(HomeViewModel.CharacterListState.ErrorState(exception))
            }
        }
    }

    @Test
    fun `onItemClick - envia personagem para tela de detalhe`() {
        val character = Character(0, "Spider-Man", "The Amazing Spider-Man", Image("", ""))

        homeViewModel.navigateToDetail.observeForTesting {
            homeViewModel.onItemClick(character)

            assertThat(homeViewModel.navigateToDetail.getOrAwaitValue().peekContent()).isEqualTo(character)
        }
    }
}