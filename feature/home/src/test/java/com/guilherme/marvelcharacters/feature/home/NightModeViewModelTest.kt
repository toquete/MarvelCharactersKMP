package com.guilherme.marvelcharacters.feature.home

import androidx.appcompat.app.AppCompatDelegate
import com.google.common.truth.Truth.assertThat
import com.guilherme.marvelcharacters.core.testing.util.BaseUnitTest
import com.guilherme.marvelcharacters.domain.usecase.GetDarkModeUseCase
import com.guilherme.marvelcharacters.domain.usecase.IsDarkModeEnabledUseCase
import com.guilherme.marvelcharacters.domain.usecase.ToggleDarkModeUseCase
import io.mockk.coEvery
import io.mockk.coVerifyOrder
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test

@ExperimentalCoroutinesApi
class NightModeViewModelTest : BaseUnitTest() {

    @RelaxedMockK
    private lateinit var getDarkModeUseCase: GetDarkModeUseCase

    @RelaxedMockK
    private lateinit var toggleDarkModeUseCase: ToggleDarkModeUseCase

    @RelaxedMockK
    private lateinit var isDarkModeEnabledUseCase: IsDarkModeEnabledUseCase

    private lateinit var nightModeViewModel: NightModeViewModel

    @Test
    fun `init - get night mode`() {
        coEvery { getDarkModeUseCase() } returns AppCompatDelegate.MODE_NIGHT_YES

        nightModeViewModel = getViewModel()

        assertThat(nightModeViewModel.nightMode.value).isEqualTo(AppCompatDelegate.MODE_NIGHT_YES)
    }

    @Test
    fun `toggleDarkMode - change dark mode`() {
        coEvery { isDarkModeEnabledUseCase() } returns true

        nightModeViewModel = getViewModel()

        nightModeViewModel.toggleDarkMode()

        coVerifyOrder {
            isDarkModeEnabledUseCase()
            toggleDarkModeUseCase(isEnabled = false)
            getDarkModeUseCase()
        }
    }

    private fun getViewModel(): NightModeViewModel {
        return NightModeViewModel(
            getDarkModeUseCase,
            toggleDarkModeUseCase,
            isDarkModeEnabledUseCase
        )
    }
}