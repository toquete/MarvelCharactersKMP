package com.guilherme.marvelcharacters.cache

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit

private const val PREFERENCE_NIGHT_MODE = "PREFERENCE_NIGHT_MODE"
private const val PREFERENCE_NIGHT_MODE_DEFAULT = AppCompatDelegate.MODE_NIGHT_NO

internal class NightModeLocalDataSourceImpl(
    private val sharedPreferences: SharedPreferences
) : NightModeLocalDataSource {

    override suspend fun isDarkModeEnabled(): Boolean {
        return getDarkMode() == AppCompatDelegate.MODE_NIGHT_YES
    }

    override suspend fun setDarkModeEnabled(isEnabled: Boolean) {
        sharedPreferences.edit {
            val mode = if (isEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }

            putInt(PREFERENCE_NIGHT_MODE, mode)
        }
    }

    override suspend fun getDarkMode(): Int {
        return sharedPreferences.getInt(
            PREFERENCE_NIGHT_MODE,
            PREFERENCE_NIGHT_MODE_DEFAULT
        )
    }
}