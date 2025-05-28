package com.guilherme.marvelcharacters.domain

import com.guilherme.marvelcharacters.data.repository.NightModeRepository
import com.guilherme.marvelcharacters.domain.usecase.ToggleDarkModeUseCase
import dev.mokkery.MockMode
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

@ExperimentalCoroutinesApi
class ToggleDarkModeUseCaseTest {

    private val nightModeRepository: NightModeRepository = mock(MockMode.autofill)
    private val toggleDarkModeUseCase = ToggleDarkModeUseCase(nightModeRepository)

    @Test
    fun `invoke - check repository was called`() = runTest {
        toggleDarkModeUseCase(isEnabled = true)

        verifySuspend { nightModeRepository.setDarkModeEnabled(isEnabled = true) }
    }
}