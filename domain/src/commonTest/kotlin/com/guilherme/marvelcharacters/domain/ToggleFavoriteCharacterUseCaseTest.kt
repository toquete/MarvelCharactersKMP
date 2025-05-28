package com.guilherme.marvelcharacters.domain

import com.guilherme.marvelcharacters.data.repository.CharacterRepository
import com.guilherme.marvelcharacters.domain.usecase.ToggleFavoriteCharacterUseCase
import dev.mokkery.MockMode
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

@ExperimentalCoroutinesApi
class ToggleFavoriteCharacterUseCaseTest {

    private val repository: CharacterRepository = mock(MockMode.autofill)
    private val useCase = ToggleFavoriteCharacterUseCase(repository)

    @Test
    fun `invoke - call deletion when character is favorite`() = runTest {
        useCase(id = 0, isFavorite = true)

        verifySuspend { repository.deleteFavoriteCharacter(id = 0) }
    }

    @Test
    fun `invoke - call insertion when character is not favorite`() = runTest {
        useCase(id = 0, isFavorite = false)

        verifySuspend { repository.insertFavoriteCharacter(id = 0) }
    }
}