package com.guilherme.marvelcharacters.domain

import com.google.common.truth.Truth.assertThat
import com.guilherme.marvelcharacters.data.repository.CharacterRepository
import com.guilherme.marvelcharacters.domain.usecase.GetFavoriteCharactersUseCase
import com.guilherme.marvelcharacters.domain.util.Fixtures
import io.mockk.MockKAnnotations
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class GetFavoriteCharactersUseCaseTest {

    @RelaxedMockK
    private lateinit var characterRepository: CharacterRepository

    @InjectMockKs
    private lateinit var getFavoriteCharactersUseCase: GetFavoriteCharactersUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `invoke - check list is returned`() = runTest {
        every { characterRepository.getFavoriteCharacters() } returns flowOf(Fixtures.characterList)

        val result = getFavoriteCharactersUseCase()

        coVerify { characterRepository.getFavoriteCharacters() }
        assertThat(result.first()).containsExactly(Fixtures.character)
    }
}