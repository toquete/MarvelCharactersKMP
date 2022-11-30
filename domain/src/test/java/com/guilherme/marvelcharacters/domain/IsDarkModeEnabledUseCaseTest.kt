package com.guilherme.marvelcharacters.domain

import com.google.common.truth.Truth.assertThat
import com.guilherme.marvelcharacters.data.repository.NightModeRepository
import com.guilherme.marvelcharacters.domain.usecase.IsDarkModeEnabledUseCase
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class IsDarkModeEnabledUseCaseTest {

    @RelaxedMockK
    private lateinit var nightModeRepository: NightModeRepository

    @InjectMockKs
    private lateinit var isDarkModeUseCase: IsDarkModeEnabledUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `invoke - check value is returned`() {
        every { nightModeRepository.isDarkModeEnabled() } returns true

        val result = isDarkModeUseCase()

        verify { nightModeRepository.isDarkModeEnabled() }
        assertThat(result).isTrue()
    }
}