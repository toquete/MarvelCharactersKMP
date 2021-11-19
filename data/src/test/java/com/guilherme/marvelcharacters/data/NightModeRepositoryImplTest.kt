package com.guilherme.marvelcharacters.data

import com.google.common.truth.Truth.assertThat
import com.guilherme.marvelcharacters.data.repository.NightModeRepositoryImpl
import com.guilherme.marvelcharacters.data.source.local.NightModeLocalDataSource
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class NightModeRepositoryImplTest {

    @RelaxedMockK
    private lateinit var nightModeLocalDataSource: NightModeLocalDataSource

    @InjectMockKs
    private lateinit var nightModeRepository: NightModeRepositoryImpl

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

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
        val mode = 1
        every { nightModeLocalDataSource.getDarkMode() } returns mode

        val result = nightModeRepository.getDarkMode()

        verify { nightModeLocalDataSource.getDarkMode() }
        assertThat(result).isEqualTo(mode)
    }
}