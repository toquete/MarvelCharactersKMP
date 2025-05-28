package com.guilherme.marvelcharacters.domain

import com.guilherme.marvelcharacters.data.repository.CharacterRepository
import com.guilherme.marvelcharacters.domain.model.FavoriteCharacter
import com.guilherme.marvelcharacters.domain.usecase.GetFavoriteCharacterByIdUseCase
import com.guilherme.marvelcharacters.domain.util.Fixtures
import dev.mokkery.MockMode
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class GetFavoriteCharacterByIdUseCaseTest {

    private val characterRepository: CharacterRepository = mock(MockMode.autofill)
    private val getFavoriteCharacterByIdUseCase = GetFavoriteCharacterByIdUseCase(characterRepository)

    @Test
    fun `invoke - check character is returned`() = runTest {
        val id = 0
        val expected = FavoriteCharacter(Fixtures.character, isFavorite = true)

        everySuspend { characterRepository.getCharacterById(id) } returns Fixtures.character
        every { characterRepository.isCharacterFavorite(id) } returns flowOf(true)

        val result = getFavoriteCharacterByIdUseCase(id)

        assertEquals(expected, result.first())
        verifySuspend { characterRepository.getCharacterById(id) }
    }
}