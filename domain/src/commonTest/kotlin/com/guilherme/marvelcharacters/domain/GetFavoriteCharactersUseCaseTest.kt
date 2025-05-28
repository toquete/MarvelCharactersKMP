package com.guilherme.marvelcharacters.domain

import com.guilherme.marvelcharacters.data.repository.CharacterRepository
import com.guilherme.marvelcharacters.domain.usecase.GetFavoriteCharactersUseCase
import com.guilherme.marvelcharacters.domain.util.Fixtures
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
import kotlin.test.assertContains

@ExperimentalCoroutinesApi
class GetFavoriteCharactersUseCaseTest {

    private val characterRepository: CharacterRepository = mock(MockMode.autofill)
    private val getFavoriteCharactersUseCase = GetFavoriteCharactersUseCase(characterRepository)

    @Test
    fun `invoke - check list is returned`() = runTest {
        every { characterRepository.getFavoriteCharacters() } returns flowOf(Fixtures.characterList)

        val result = getFavoriteCharactersUseCase()

        verifySuspend { characterRepository.getFavoriteCharacters() }
        assertContains(result.first(), Fixtures.character)
    }
}