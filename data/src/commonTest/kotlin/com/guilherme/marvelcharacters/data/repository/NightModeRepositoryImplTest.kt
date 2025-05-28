package com.guilherme.marvelcharacters.data.repository

import com.guilherme.marvelcharacters.cache.NightModeLocalDataSource
import dev.mokkery.MockMode
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class NightModeRepositoryImplTest {

    private val nightModeLocalDataSource: NightModeLocalDataSource = mock(MockMode.autofill)
    private val nightModeRepository = NightModeRepositoryImpl(nightModeLocalDataSource)

    @Test
    fun `isDarkModeEnabled - return value`() = runTest {
        everySuspend { nightModeLocalDataSource.isDarkModeEnabled() } returns true

        val result = nightModeRepository.isDarkModeEnabled()

        verifySuspend { nightModeLocalDataSource.isDarkModeEnabled() }
        assertTrue(result)
    }

    @Test
    fun `setDarkModeEnabled - check data source was called`() = runTest {
        nightModeRepository.setDarkModeEnabled(isEnabled = true)

        verifySuspend { nightModeLocalDataSource.setDarkModeEnabled(isEnabled = true) }
    }

    @Test
    fun `getDarkMode - return value`() = runTest {
        val mode = 1
        everySuspend { nightModeLocalDataSource.getDarkMode() } returns mode

        val result = nightModeRepository.getDarkMode()

        verifySuspend { nightModeLocalDataSource.getDarkMode() }
        assertEquals(mode, result)
    }
}