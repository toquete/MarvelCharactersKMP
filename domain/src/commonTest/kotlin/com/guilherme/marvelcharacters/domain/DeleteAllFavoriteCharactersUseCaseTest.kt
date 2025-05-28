package com.guilherme.marvelcharacters.domain

import com.guilherme.marvelcharacters.data.repository.CharacterRepository
import com.guilherme.marvelcharacters.domain.usecase.DeleteAllFavoriteCharactersUseCase
import dev.mokkery.MockMode
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

@ExperimentalCoroutinesApi
class DeleteAllFavoriteCharactersUseCaseTest {

    private val characterRepository: CharacterRepository = mock(MockMode.autofill)
    private val deleteAllFavoriteCharactersUseCase = DeleteAllFavoriteCharactersUseCase(characterRepository)

    @Test
    fun `invoke - check repository was called`() = runTest {
        deleteAllFavoriteCharactersUseCase()

        verifySuspend { characterRepository.deleteAllFavoriteCharacters() }
    }
}