package com.guilherme.marvelcharacters.data.repository

import com.google.common.truth.Truth.assertThat
import com.guilherme.marvelcharacters.cache.NightModeLocalDataSource
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.test.runTest
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
    fun `isDarkModeEnabled - return value`() = runTest {
        coEvery { nightModeLocalDataSource.isDarkModeEnabled() } returns true

        val result = nightModeRepository.isDarkModeEnabled()

        coVerify { nightModeLocalDataSource.isDarkModeEnabled() }
        assertThat(result).isTrue()
    }

    @Test
    fun `setDarkModeEnabled - check data source was called`() = runTest {
        nightModeRepository.setDarkModeEnabled(isEnabled = true)

        coVerify { nightModeLocalDataSource.setDarkModeEnabled(isEnabled = true) }
    }

    @Test
    fun `getDarkMode - return value`() = runTest {
        val mode = 1
        coEvery { nightModeLocalDataSource.getDarkMode() } returns mode

        val result = nightModeRepository.getDarkMode()

        coVerify { nightModeLocalDataSource.getDarkMode() }
        assertThat(result).isEqualTo(mode)
    }
}