package com.guilherme.marvelcharacters

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.guilherme.marvelcharacters.data.model.Character
import com.guilherme.marvelcharacters.data.repository.CharacterRepository
import com.guilherme.marvelcharacters.infrastructure.TestCoroutineRule
import com.guilherme.marvelcharacters.ui.main.MainViewModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class MainViewModelTest {

    lateinit var viewModel: MainViewModel

    @RelaxedMockK
    lateinit var statesObserver: Observer<MainViewModel.CharacterListState>

    @RelaxedMockK
    lateinit var characterRepository: CharacterRepository

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = MainViewModel(characterRepository, testCoroutineRule.testCoroutineDispatcher).apply {
            states.observeForever(statesObserver)
        }
    }

    @Test
    fun onSearchCharacter_getCharacterList_success() = testCoroutineRule.runBlockingTest {
        val character = Character(0, "Spider-Man", "The Amazing Spider-Man")
        val characterList = listOf(character)

        coEvery { characterRepository.getCharacters(any()) } returns characterList

        viewModel.onSearchCharacter("spider")

        verify { statesObserver.onChanged(MainViewModel.CharacterListState.Characters(characterList)) }
    }

    @Test
    fun onSearchCharacter_getCharacterList_emptyList() = testCoroutineRule.runBlockingTest {
        val characterList = emptyList<Character>()

        coEvery { characterRepository.getCharacters(any()) } returns characterList

        viewModel.onSearchCharacter("spider")

        verify { statesObserver.onChanged(MainViewModel.CharacterListState.EmptyState) }
    }

    @Test
    fun onSearchCharacter_getCharacterList_error() = testCoroutineRule.runBlockingTest {
        val exception = Exception("This is an error")
        coEvery { characterRepository.getCharacters(any()) } throws exception

        viewModel.onSearchCharacter("spider")

        verify { statesObserver.onChanged(MainViewModel.CharacterListState.ErrorState(exception)) }
    }
}