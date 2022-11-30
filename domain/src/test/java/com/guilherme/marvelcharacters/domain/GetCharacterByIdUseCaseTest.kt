package com.guilherme.marvelcharacters.domain

import com.google.common.truth.Truth.assertThat
import com.guilherme.marvelcharacters.core.model.Character
import com.guilherme.marvelcharacters.core.model.Image
import com.guilherme.marvelcharacters.data.repository.CharacterRepository
import com.guilherme.marvelcharacters.domain.usecase.GetCharacterByIdUseCase
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
class GetCharacterByIdUseCaseTest {

    @RelaxedMockK
    private lateinit var characterRepository: CharacterRepository

    @InjectMockKs
    private lateinit var getCharacterByIdUseCase: GetCharacterByIdUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `invoke - check character is returned`() = runBlockingTest {
        val id = 0
        val key = "123"
        val privateKey = "456"
        val character = Character(
            id = 0,
            name = "Spider-Man",
            description = "",
            thumbnail = Image(path = "", extension = "")
        )

        coEvery { characterRepository.getCharacterById(id, key, privateKey) } returns character

        val result = getCharacterByIdUseCase(id, key, privateKey)

        coVerify { characterRepository.getCharacterById(id, key, privateKey) }
        assertThat(result).isEqualTo(character)
    }
}