package com.guilherme.marvelcharacters.domain

import com.google.common.truth.Truth.assertThat
import com.guilherme.marvelcharacters.data.repository.CharacterRepository
import com.guilherme.marvelcharacters.domain.usecase.GetCharactersUseCase
import com.guilherme.marvelcharacters.domain.util.Fixtures
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class GetCharactersUseCaseTest {

    @RelaxedMockK
    private lateinit var characterRepository: CharacterRepository

    @InjectMockKs
    private lateinit var getCharactersUseCase: GetCharactersUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `invoke - check list is returned`() = runBlockingTest {
        val name = "spider"

        coEvery { characterRepository.getCharacters(name) } returns Fixtures.characterList

        val result = getCharactersUseCase(name)

        coVerify { characterRepository.getCharacters(name) }
        assertThat(result).containsExactly(Fixtures.character)
    }
}