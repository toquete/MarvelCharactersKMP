package com.guilherme.marvelcharacters.domain

import com.google.common.truth.Truth.assertThat
import com.guilherme.marvelcharacters.domain.model.Character
import com.guilherme.marvelcharacters.domain.repository.CharacterRepository
import com.guilherme.marvelcharacters.domain.usecase.GetCharactersUseCase
import io.mockk.MockKAnnotations
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
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
        val key = "123"
        val privateKey = "456"
        val list = listOf(
            Character(
                id = 0,
                name = "Spider-Man",
                description = "",
                thumbnail = ""
            )
        )
        every { characterRepository.getCharacters(name, key, privateKey) } returns flowOf(list)

        val result = getCharactersUseCase(name, key, privateKey)

        coVerify { characterRepository.getCharacters(name, key, privateKey) }
        assertThat(result.first()).isEqualTo(list)
    }
}