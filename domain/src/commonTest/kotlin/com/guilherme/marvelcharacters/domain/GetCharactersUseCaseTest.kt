package com.guilherme.marvelcharacters.domain

import com.guilherme.marvelcharacters.data.repository.CharacterRepository
import com.guilherme.marvelcharacters.domain.usecase.GetCharactersUseCase
import com.guilherme.marvelcharacters.domain.util.Fixtures
import dev.mokkery.MockMode
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertContains

@ExperimentalCoroutinesApi
class GetCharactersUseCaseTest {

    private val characterRepository: CharacterRepository = mock(MockMode.autofill)
    private val getCharactersUseCase = GetCharactersUseCase(characterRepository)

    @Test
    fun `invoke - check list is returned`() = runTest {
        val name = "spider"

        everySuspend { characterRepository.getCharacters(name) } returns Fixtures.characterList

        val result = getCharactersUseCase(name)

        verifySuspend { characterRepository.getCharacters(name) }
        assertContains(result, Fixtures.character)
    }
}