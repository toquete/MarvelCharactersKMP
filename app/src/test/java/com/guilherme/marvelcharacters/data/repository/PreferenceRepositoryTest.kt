package com.guilherme.marvelcharacters.data.repository

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.google.common.truth.Truth.assertThat
import com.guilherme.marvelcharacters.infrastructure.BaseUnitTest
import com.guilherme.marvelcharacters.util.getOrAwaitValue
import com.guilherme.marvelcharacters.util.observeForTesting
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test

@ExperimentalCoroutinesApi
class PreferenceRepositoryTest : BaseUnitTest() {

    @RelaxedMockK
    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var preferenceRepository: PreferenceRepository

    @Test
    fun `nightMode - verifica estado do sharedPreferences`() {
        preferenceRepository = PreferenceRepository(sharedPreferences)

        every { sharedPreferences.getInt(any(), any()) } returns AppCompatDelegate.MODE_NIGHT_YES

        val result = preferenceRepository.nightMode

        assertThat(result).isEqualTo(AppCompatDelegate.MODE_NIGHT_YES)
    }

    @Test
    fun `isDarkTheme - retorna true quando tema escuro`() {
        preferenceRepository = PreferenceRepository(sharedPreferences)

        every { sharedPreferences.getInt(any(), any()) } returns AppCompatDelegate.MODE_NIGHT_YES

        val result = preferenceRepository.isDarkTheme

        assertThat(result).isTrue()
    }

    @Test
    fun `isDarkTheme - retorna false quando tema claro`() {
        preferenceRepository = PreferenceRepository(sharedPreferences)

        every { sharedPreferences.getInt(any(), any()) } returns AppCompatDelegate.MODE_NIGHT_NO

        val result = preferenceRepository.isDarkTheme

        assertThat(result).isFalse()
    }

    @Test
    fun `isDarkTheme - seta tema escuro no sharedPreferences`() {
        preferenceRepository = PreferenceRepository(sharedPreferences)

        preferenceRepository.isDarkTheme = true

        verify { sharedPreferences.edit().putInt(any(), AppCompatDelegate.MODE_NIGHT_YES) }
    }

    @Test
    fun `isDarkTheme - seta tema claro no sharedPreferences`() {
        preferenceRepository = PreferenceRepository(sharedPreferences)

        preferenceRepository.isDarkTheme = false

        verify { sharedPreferences.edit().putInt(any(), AppCompatDelegate.MODE_NIGHT_NO) }
    }

    @Test
    fun `init - envia modo salvo no sharedPrefrences`() {
        every { sharedPreferences.getInt(any(), any()) } returns AppCompatDelegate.MODE_NIGHT_YES

        preferenceRepository = PreferenceRepository(sharedPreferences)

        preferenceRepository.nightModeLive.observeForTesting {
            assertThat(preferenceRepository.nightModeLive.getOrAwaitValue()).isEqualTo(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }

    @Test
    fun `init - envia estado salvo no sharedPrefrences`() {
        every { sharedPreferences.getInt(any(), any()) } returns AppCompatDelegate.MODE_NIGHT_YES

        preferenceRepository = PreferenceRepository(sharedPreferences)

        preferenceRepository.isDarkThemeLive.observeForTesting {
            assertThat(preferenceRepository.isDarkThemeLive.getOrAwaitValue()).isTrue()
        }
    }
}