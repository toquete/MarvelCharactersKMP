package com.guilherme.marvelcharacters.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.guilherme.marvelcharacters.domain.model.Character
import com.guilherme.marvelcharacters.domain.model.Image
import com.guilherme.marvelcharacters.domain.repository.CharacterRepository
import com.guilherme.marvelcharacters.infrastructure.BaseUnitTest
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

@ExperimentalCoroutinesApi
class GetCharactersUseCaseTest : BaseUnitTest() {

    @RelaxedMockK
    private lateinit var characterRepository: CharacterRepository

    @InjectMockKs
    private lateinit var getCharactersUseCase: GetCharactersUseCase

    @Test
    fun `invoke - check list is returned`() = runBlockingTest {
        val name = "spider"
        val list = listOf(
            Character(
                id = 0,
                name = "Spider-Man",
                description = "",
                thumbnail = Image(path = "", extension = "")
            )
        )
        every { characterRepository.getCharacters(name) } returns flowOf(list)

        val result = getCharactersUseCase(name)

        coVerify { characterRepository.getCharacters(name) }
        assertThat(result.first()).isEqualTo(list)
    }
}