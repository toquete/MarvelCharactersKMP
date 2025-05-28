package com.guilherme.marvelcharacters.domain

import com.guilherme.marvelcharacters.data.repository.NightModeRepository
import com.guilherme.marvelcharacters.domain.usecase.IsDarkModeEnabledUseCase
import dev.mokkery.MockMode
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
class IsDarkModeEnabledUseCaseTest {

    private val nightModeRepository: NightModeRepository = mock(MockMode.autofill)
    private val isDarkModeUseCase = IsDarkModeEnabledUseCase(nightModeRepository)

    @Test
    fun `invoke - check value is returned`() = runTest {
        everySuspend { nightModeRepository.isDarkModeEnabled() } returns true

        val result = isDarkModeUseCase()

        verifySuspend { nightModeRepository.isDarkModeEnabled() }
        assertTrue(result)
    }
}