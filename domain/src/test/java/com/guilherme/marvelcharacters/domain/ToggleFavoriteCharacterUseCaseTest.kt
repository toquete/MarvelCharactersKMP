package com.guilherme.marvelcharacters.domain

import com.guilherme.marvelcharacters.data.repository.CharacterRepository
import com.guilherme.marvelcharacters.domain.usecase.ToggleFavoriteCharacterUseCase
import io.mockk.MockKAnnotations
import io.mockk.coVerify
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class ToggleFavoriteCharacterUseCaseTest {

    @RelaxedMockK
    private lateinit var repository: CharacterRepository

    @InjectMockKs
    private lateinit var useCase: ToggleFavoriteCharacterUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `invoke - call deletion when character is favorite`() = runTest {
        useCase(id = 0, isFavorite = true)

        coVerify { repository.deleteFavoriteCharacter(id = 0) }
    }

    @Test
    fun `invoke - call insertion when character is not favorite`() = runTest {
        useCase(id = 0, isFavorite = false)

        coVerify { repository.insertFavoriteCharacter(id = 0) }
    }
}