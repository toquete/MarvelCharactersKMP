package com.guilherme.marvelcharacters.domain

import com.google.common.truth.Truth.assertThat
import com.guilherme.marvelcharacters.core.model.Character
import com.guilherme.marvelcharacters.core.model.Image
import com.guilherme.marvelcharacters.data.repository.CharacterRepository
import com.guilherme.marvelcharacters.domain.model.FavoriteCharacter
import com.guilherme.marvelcharacters.domain.usecase.GetFavoriteCharacterByIdUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
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
class GetFavoriteCharacterByIdUseCaseTest {

    @RelaxedMockK
    private lateinit var characterRepository: CharacterRepository

    @InjectMockKs
    private lateinit var getFavoriteCharacterByIdUseCase: GetFavoriteCharacterByIdUseCase

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
        val expected = FavoriteCharacter(character, isFavorite = true)

        coEvery { characterRepository.getCharacterById(id, key, privateKey) } returns character
        every { characterRepository.isCharacterFavorite(id) } returns flowOf(true)

        val result = getFavoriteCharacterByIdUseCase(id, key, privateKey)

        coVerify { characterRepository.getCharacterById(id, key, privateKey) }
        assertThat(result.first()).isEqualTo(expected)
    }
}