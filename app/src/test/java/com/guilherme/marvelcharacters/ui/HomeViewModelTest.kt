package com.guilherme.marvelcharacters.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.google.common.truth.Truth.assertThat
import com.guilherme.marvelcharacters.data.model.Character
import com.guilherme.marvelcharacters.data.model.Image
import com.guilherme.marvelcharacters.data.repository.CharacterRepository
import com.guilherme.marvelcharacters.infrastructure.TestCoroutineRule
import com.guilherme.marvelcharacters.ui.home.HomeViewModel
import com.guilherme.marvelcharacters.util.getOrAwaitValue
import com.guilherme.marvelcharacters.util.observeForTesting
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verifySequence
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class HomeViewModelTest {

    lateinit var viewModel: HomeViewModel

    @RelaxedMockK
    lateinit var characterRepository: CharacterRepository

    @RelaxedMockK
    lateinit var observer: Observer<HomeViewModel.CharacterListState>

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = HomeViewModel(characterRepository, testCoroutineRule.testCoroutineDispatcher)
    }

    @Test
    fun `onSearchCharacter - envia estado de sucesso ao carregar a lista`() = testCoroutineRule.runBlockingTest {
        val character = Character(0, "Spider-Man", "The Amazing Spider-Man", Image("", ""))
        val characterList = listOf(character)

        coEvery { characterRepository.getCharacters(any()) } returns characterList

        viewModel.states.observeForTesting(observer) {
            viewModel.onSearchCharacter("spider")

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

        viewModel.states.observeForTesting(observer) {
            viewModel.onSearchCharacter("spider")

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

        viewModel.states.observeForTesting(observer) {
            viewModel.onSearchCharacter("spider")

            verifySequence {
                observer.onChanged(HomeViewModel.CharacterListState.Loading)
                observer.onChanged(HomeViewModel.CharacterListState.ErrorState(exception))
            }
        }
    }

    @Test
    fun `onItemClick - envia personagem para tela de detalhe`() {
        val character = Character(0, "Spider-Man", "The Amazing Spider-Man", Image("", ""))

        viewModel.navigateToDetail.observeForTesting {
            viewModel.onItemClick(character)

            assertThat(viewModel.navigateToDetail.getOrAwaitValue().peekContent()).isEqualTo(character)
        }
    }
}