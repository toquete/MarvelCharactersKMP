package com.guilherme.marvelcharacters.domain.usecase

import com.guilherme.marvelcharacters.domain.repository.NightModeRepository
import com.guilherme.marvelcharacters.infrastructure.BaseUnitTest
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test

@ExperimentalCoroutinesApi
class ToggleDarkModeUseCaseTest : BaseUnitTest() {

    @RelaxedMockK
    private lateinit var nightModeRepository: NightModeRepository

    @InjectMockKs
    private lateinit var toggleDarkModeUseCase: ToggleDarkModeUseCase

    @Test
    fun `invoke - check repository was called`() {
        toggleDarkModeUseCase(isEnabled = true)

        verify { nightModeRepository.setDarkModeEnabled(isEnabled = true) }
    }
}