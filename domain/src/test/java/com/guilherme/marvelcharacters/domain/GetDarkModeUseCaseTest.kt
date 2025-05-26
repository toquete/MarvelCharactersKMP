package com.guilherme.marvelcharacters.domain

import com.google.common.truth.Truth.assertThat
import com.guilherme.marvelcharacters.data.repository.NightModeRepository
import com.guilherme.marvelcharacters.domain.usecase.GetDarkModeUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
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
    fun `invoke - check mode is returned`() = runTest {
        val mode = 1
        coEvery { nightModeRepository.getDarkMode() } returns mode

        val result = getDarkModeUseCase()

        coVerify { nightModeRepository.getDarkMode() }
        assertThat(result).isEqualTo(mode)
    }
}