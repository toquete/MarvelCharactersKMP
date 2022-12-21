package com.guilherme.marvelcharacters.domain

import com.guilherme.marvelcharacters.data.repository.CharacterRepository
import com.guilherme.marvelcharacters.domain.usecase.DeleteAllFavoriteCharactersUseCase
import io.mockk.MockKAnnotations
import io.mockk.coVerify
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class DeleteAllFavoriteCharactersUseCaseTest {

    @RelaxedMockK
    private lateinit var characterRepository: CharacterRepository

    @InjectMockKs
    private lateinit var deleteAllFavoriteCharactersUseCase: DeleteAllFavoriteCharactersUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `invoke - check repository was called`() = runTest {
        deleteAllFavoriteCharactersUseCase()

        coVerify { characterRepository.deleteAllFavoriteCharacters() }
    }
}