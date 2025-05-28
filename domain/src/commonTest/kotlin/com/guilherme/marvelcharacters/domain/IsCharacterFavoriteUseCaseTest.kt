package com.guilherme.marvelcharacters.domain

import com.guilherme.marvelcharacters.data.repository.CharacterRepository
import com.guilherme.marvelcharacters.domain.usecase.IsCharacterFavoriteUseCase
import dev.mokkery.MockMode
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
class IsCharacterFavoriteUseCaseTest {

    private val characterRepository: CharacterRepository = mock(MockMode.autofill)
    private val isCharacterFavoriteUseCase = IsCharacterFavoriteUseCase(characterRepository)

    @Test
    fun `invoke - check value is returned`() = runTest {
        val id = 0
        every { characterRepository.isCharacterFavorite(id) } returns flowOf(true)

        val result = isCharacterFavoriteUseCase(id)

        verifySuspend { characterRepository.isCharacterFavorite(id) }
        assertTrue(result.first())
    }
}