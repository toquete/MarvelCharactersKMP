package com.guilherme.marvelcharacters

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.guilherme.marvelcharacters.data.model.Character
import com.guilherme.marvelcharacters.data.model.Image
import com.guilherme.marvelcharacters.data.repository.CharacterRepository
import com.guilherme.marvelcharacters.infrastructure.TestCoroutineRule
import com.guilherme.marvelcharacters.ui.home.HomeViewModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class HomeViewModelTest {

    lateinit var viewModel: HomeViewModel

    @RelaxedMockK
    lateinit var statesObserver: Observer<HomeViewModel.CharacterListState>

    @RelaxedMockK
    lateinit var characterRepository: CharacterRepository

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = HomeViewModel(characterRepository, testCoroutineRule.testCoroutineDispatcher, mockk(relaxed = true)).apply {
            states.observeForever(statesObserver)
        }
    }

    @Test
    fun onSearchCharacter_getCharacterList_success() = testCoroutineRule.runBlockingTest {
        val character = Character(0, "Spider-Man", "The Amazing Spider-Man", Image("", ""))
        val characterList = listOf(character)

        coEvery { characterRepository.getCharacters(any()) } returns characterList

        viewModel.onSearchCharacter("spider")

        verify { statesObserver.onChanged(HomeViewModel.CharacterListState.Characters(characterList)) }
    }

    @Test
    fun onSearchCharacter_getCharacterList_emptyList() = testCoroutineRule.runBlockingTest {
        val characterList = emptyList<Character>()

        coEvery { characterRepository.getCharacters(any()) } returns characterList

        viewModel.onSearchCharacter("spider")

        verify { statesObserver.onChanged(HomeViewModel.CharacterListState.EmptyState) }
    }

    @Test
    fun onSearchCharacter_getCharacterList_error() = testCoroutineRule.runBlockingTest {
        val exception = Exception("This is an error")
        coEvery { characterRepository.getCharacters(any()) } throws exception

        viewModel.onSearchCharacter("spider")

        verify { statesObserver.onChanged(HomeViewModel.CharacterListState.ErrorState(exception)) }
    }
}