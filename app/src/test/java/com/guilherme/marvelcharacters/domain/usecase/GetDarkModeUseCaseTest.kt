package com.guilherme.marvelcharacters.domain.usecase

import androidx.appcompat.app.AppCompatDelegate
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
class GetDarkModeUseCaseTest : BaseUnitTest() {

    @RelaxedMockK
    private lateinit var nightModeRepository: NightModeRepository

    @InjectMockKs
    private lateinit var getDarkModeUseCase: GetDarkModeUseCase

    @Test
    fun `invoke - check mode is returned`() {
        val mode = AppCompatDelegate.MODE_NIGHT_NO
        every { nightModeRepository.getDarkMode() } returns mode

        val result = getDarkModeUseCase()

        verify { nightModeRepository.getDarkMode() }
        assertThat(result).isEqualTo(mode)
    }
}