package com.guilherme.marvelcharacters.domain

import com.guilherme.marvelcharacters.data.repository.NightModeRepository
import com.guilherme.marvelcharacters.domain.usecase.GetDarkModeUseCase
import dev.mokkery.MockMode
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class GetDarkModeUseCaseTest {

    private val nightModeRepository: NightModeRepository = mock(MockMode.autofill)
    private val getDarkModeUseCase = GetDarkModeUseCase(nightModeRepository)

    @Test
    fun `invoke - check mode is returned`() = runTest {
        val mode = 1
        everySuspend { nightModeRepository.getDarkMode() } returns mode

        val result = getDarkModeUseCase()

        verifySuspend { nightModeRepository.getDarkMode() }
        assertEquals(mode, result)
    }
}