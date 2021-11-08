package com.guilherme.marvelcharacters.data.repository

import androidx.appcompat.app.AppCompatDelegate
import com.google.common.truth.Truth.assertThat
import com.guilherme.marvelcharacters.data.source.local.NightModeLocalDataSource
import com.guilherme.marvelcharacters.infrastructure.BaseUnitTest
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test

@ExperimentalCoroutinesApi
class NightModeRepositoryImplTest : BaseUnitTest() {

    @RelaxedMockK
    private lateinit var nightModeLocalDataSource: NightModeLocalDataSource

    @InjectMockKs
    private lateinit var nightModeRepository: NightModeRepositoryImpl

    @Test
    fun `isDarkModeEnabled - return value`() {
        every { nightModeLocalDataSource.isDarkModeEnabled() } returns true

        val result = nightModeRepository.isDarkModeEnabled()

        verify { nightModeLocalDataSource.isDarkModeEnabled() }
        assertThat(result).isTrue()
    }

    @Test
    fun `setDarkModeEnabled - check data source was called`() {
        nightModeRepository.setDarkModeEnabled(isEnabled = true)

        verify { nightModeLocalDataSource.setDarkModeEnabled(isEnabled = true) }
    }

    @Test
    fun `getDarkMode - return value`() {
        val mode = AppCompatDelegate.MODE_NIGHT_NO
        every { nightModeLocalDataSource.getDarkMode() } returns mode

        val result = nightModeRepository.getDarkMode()

        verify { nightModeLocalDataSource.getDarkMode() }
        assertThat(result).isEqualTo(mode)
    }
}