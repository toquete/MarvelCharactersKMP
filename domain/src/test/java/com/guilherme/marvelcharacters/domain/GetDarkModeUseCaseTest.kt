package com.guilherme.marvelcharacters.domain

import com.google.common.truth.Truth.assertThat
import com.guilherme.marvelcharacters.data.repository.NightModeRepository
import com.guilherme.marvelcharacters.domain.usecase.GetDarkModeUseCase
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class GetDarkModeUseCaseTest {

    @RelaxedMockK
    private lateinit var nightModeRepository: NightModeRepository

    @InjectMockKs
    private lateinit var getDarkModeUseCase: GetDarkModeUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `invoke - check mode is returned`() {
        val mode = 1
        every { nightModeRepository.getDarkMode() } returns mode

        val result = getDarkModeUseCase()

        verify { nightModeRepository.getDarkMode() }
        assertThat(result).isEqualTo(mode)
    }
}