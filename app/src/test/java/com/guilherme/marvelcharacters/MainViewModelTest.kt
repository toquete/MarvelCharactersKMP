package com.guilherme.marvelcharacters

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import com.guilherme.marvelcharacters.data.model.Character
import com.guilherme.marvelcharacters.data.repository.CharacterRepository
import com.guilherme.marvelcharacters.ui.main.MainViewModel
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import java.lang.Exception

@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {

    lateinit var viewModel: MainViewModel

    @Mock
    lateinit var statesObserver: Observer<MainViewModel.CharacterListState>

    @Mock
    lateinit var characterRepository: CharacterRepository

    @Captor
    lateinit var statesCaptor: ArgumentCaptor<MainViewModel.CharacterListState>

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        viewModel = MainViewModel(characterRepository, Dispatchers.Unconfined).apply {
            states.observeForever(statesObserver)
        }
    }

    @Test
    fun onSearchCharacter_getCharacterList_success() = runBlocking {
        val character = Character(0, "Spider-Man", "The Amazing Spider-Man")
        val characterList = listOf(character)

        whenever(characterRepository.getCharacters(any())).thenReturn(characterList)

        viewModel.onSearchCharacter("spider")

        verify(statesObserver).onChanged(statesCaptor.capture())

        with(statesCaptor.allValues) {
            assertEquals(1, this.size)
            assertEquals(MainViewModel.CharacterListState.Characters(characterList), this[0])
        }
    }

    @Test
    fun onSearchCharacter_getCharacterList_emptyList() = runBlocking {
        val characterList = emptyList<Character>()

        whenever(characterRepository.getCharacters(any())).thenReturn(characterList)

        viewModel.onSearchCharacter("spider")

        verify(statesObserver).onChanged(statesCaptor.capture())

        with(statesCaptor.allValues) {
            assertEquals(1, this.size)
            assertEquals(MainViewModel.CharacterListState.EmptyState, this[0])
        }
    }

    @Test
    fun onSearchCharacter_getCharacterList_error() = runBlocking {
        val exception = Exception("This is an error")
        whenever(characterRepository.getCharacters(any())).then { throw exception }

        viewModel.onSearchCharacter("spider")

        verify(statesObserver).onChanged(statesCaptor.capture())

        with(statesCaptor.allValues) {
            assertEquals(1, this.size)
            assertEquals(MainViewModel.CharacterListState.ErrorState(exception), this[0])
        }
    }
}