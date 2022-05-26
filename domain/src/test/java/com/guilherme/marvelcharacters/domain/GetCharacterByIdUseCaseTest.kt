package com.guilherme.marvelcharacters.domain

import com.google.common.truth.Truth.assertThat
import com.guilherme.marvelcharacters.domain.model.Character
import com.guilherme.marvelcharacters.domain.repository.CharacterRepository
import com.guilherme.marvelcharacters.domain.usecase.GetCharacterByIdUseCase
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class GetCharacterByIdUseCaseTest {

    @RelaxedMockK
    private lateinit var characterRepository: CharacterRepository

    @InjectMockKs
    private lateinit var getFavoriteCharacterByIdUseCase: GetCharacterByIdUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `invoke - check character is returned`() = runBlockingTest {
        val character = Character(
            id = 0,
            name = "Spider-Man",
            description = "",
            thumbnail = ""
        )
        every { characterRepository.getCharacter(id = 0) } returns flowOf(character)

        val result = getFavoriteCharacterByIdUseCase(id = 0)

        verify { characterRepository.getCharacter(id = 0) }
        assertThat(result.first()).isEqualTo(character)
    }
}