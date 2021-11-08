package com.guilherme.marvelcharacters.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.guilherme.marvelcharacters.domain.repository.NightModeRepository
import com.guilherme.marvelcharacters.infrastructure.BaseUnitTest
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test

@ExperimentalCoroutinesApi
class IsDarkModeEnabledUseCaseTest : BaseUnitTest() {

    @RelaxedMockK
    private lateinit var nightModeRepository: NightModeRepository

    @InjectMockKs
    private lateinit var isDarkModeUseCase: IsDarkModeEnabledUseCase

    @Test
    fun `invoke - check value is returned`() {
        every { nightModeRepository.isDarkModeEnabled() } returns true

        val result = isDarkModeUseCase()

        verify { nightModeRepository.isDarkModeEnabled() }
        assertThat(result).isTrue()
    }
}