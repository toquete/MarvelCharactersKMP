package com.guilherme.marvelcharacters.domain

import com.google.common.truth.Truth.assertThat
import com.guilherme.marvelcharacters.data.repository.CharacterRepository
import com.guilherme.marvelcharacters.domain.model.FavoriteCharacter
import com.guilherme.marvelcharacters.domain.usecase.GetFavoriteCharacterByIdUseCase
import com.guilherme.marvelcharacters.domain.util.Fixtures
import io.mockk.MockKAnnotations
import io.mockk.coEvery
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
    fun `invoke - check character is returned`() = runTest {
        val id = 0
        val expected = FavoriteCharacter(Fixtures.character, isFavorite = true)

        coEvery { characterRepository.getCharacterById(id) } returns Fixtures.character
        every { characterRepository.isCharacterFavorite(id) } returns flowOf(true)

        val result = getFavoriteCharacterByIdUseCase(id)

        assertThat(result.first()).isEqualTo(expected)
        coVerify { characterRepository.getCharacterById(id) }
    }
}