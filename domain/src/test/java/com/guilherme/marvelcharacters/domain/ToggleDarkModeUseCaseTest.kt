package com.guilherme.marvelcharacters.domain

import com.guilherme.marvelcharacters.data.repository.NightModeRepository
import com.guilherme.marvelcharacters.domain.usecase.ToggleDarkModeUseCase
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class ToggleDarkModeUseCaseTest {

    @RelaxedMockK
    private lateinit var nightModeRepository: NightModeRepository

    @InjectMockKs
    private lateinit var toggleDarkModeUseCase: ToggleDarkModeUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `invoke - check repository was called`() {
        toggleDarkModeUseCase(isEnabled = true)

        verify { nightModeRepository.setDarkModeEnabled(isEnabled = true) }
    }
}