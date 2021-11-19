package com.guilherme.marvelcharacters.domain

import com.google.common.truth.Truth.assertThat
import com.guilherme.marvelcharacters.domain.repository.CharacterRepository
import com.guilherme.marvelcharacters.domain.usecase.IsCharacterFavoriteUseCase
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
class IsCharacterFavoriteUseCaseTest {

    @RelaxedMockK
    private lateinit var characterRepository: CharacterRepository

    @InjectMockKs
    private lateinit var isCharacterFavoriteUseCase: IsCharacterFavoriteUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `invoke - check value is returned`() = runBlockingTest {
        val id = 0
        every { characterRepository.isCharacterFavorite(id) } returns flowOf(true)

        val result = isCharacterFavoriteUseCase(id)

        coVerify { characterRepository.isCharacterFavorite(id) }
        assertThat(result.first()).isTrue()
    }
}