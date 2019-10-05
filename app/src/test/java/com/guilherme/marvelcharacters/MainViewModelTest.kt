package com.guilherme.marvelcharacters

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.guilherme.marvelcharacters.data.model.Character
import com.guilherme.marvelcharacters.data.repository.CharacterRepository
import com.guilherme.marvelcharacters.ui.main.MainViewModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MainViewModelTest {

    lateinit var viewModel: MainViewModel

    @MockK(relaxed = true)
    lateinit var statesObserver: Observer<MainViewModel.CharacterListState>

    @MockK(relaxed = true)
    lateinit var characterRepository: CharacterRepository

    private val statesCaptor = mutableListOf<MainViewModel.CharacterListState>()

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = MainViewModel(characterRepository, Dispatchers.Unconfined).apply {
            states.observeForever(statesObserver)
        }
    }

    @Test
    fun onSearchCharacter_getCharacterList_success() {
        val character = Character(0, "Spider-Man", "The Amazing Spider-Man")
        val characterList = listOf(character)

        coEvery { characterRepository.getCharacters(any()) } returns characterList

        viewModel.onSearchCharacter("spider")

        verify { statesObserver.onChanged(capture(statesCaptor)) }

        with(statesCaptor) {
            assertEquals(1, this.size)
            assertEquals(MainViewModel.CharacterListState.Characters(characterList), this[0])
        }
    }

    @Test
    fun onSearchCharacter_getCharacterList_emptyList() {
        val characterList = emptyList<Character>()

        coEvery { characterRepository.getCharacters(any()) } returns characterList

        viewModel.onSearchCharacter("spider")

        verify { statesObserver.onChanged(capture(statesCaptor)) }

        with(statesCaptor) {
            assertEquals(1, this.size)
            assertEquals(MainViewModel.CharacterListState.EmptyState, this[0])
        }
    }

    @Test
    fun onSearchCharacter_getCharacterList_error() {
        val exception = Exception("This is an error")
        coEvery { characterRepository.getCharacters(any()) } throws exception

        viewModel.onSearchCharacter("spider")

        verify { statesObserver.onChanged(capture(statesCaptor)) }

        with(statesCaptor) {
            assertEquals(1, this.size)
            assertEquals(MainViewModel.CharacterListState.ErrorState(exception), this[0])
        }
    }
}