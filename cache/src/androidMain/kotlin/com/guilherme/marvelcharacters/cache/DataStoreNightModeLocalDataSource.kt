package com.guilherme.marvelcharacters.cache

import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.guilherme.marvelcharacters.cache.PreferencesKeys.DARK_THEME_CONFIG
import com.guilherme.marvelcharacters.core.model.UserPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

internal class DataStoreNightModeLocalDataSource(
    private val dataStore: DataStore<Preferences>
) : NightModeLocalDataSource {

    private val userPreferences: Flow<UserPreferences> = dataStore.data.map { preferences ->
        val nightMode = preferences[DARK_THEME_CONFIG] ?: AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM

        UserPreferences(nightMode)
    }

    override suspend fun isDarkModeEnabled(): Boolean {
        return userPreferences.firstOrNull()?.nightMode == AppCompatDelegate.MODE_NIGHT_YES
    }

    override suspend fun setDarkModeEnabled(isEnabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[DARK_THEME_CONFIG] = if (isEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        }
    }

    override suspend fun getDarkMode(): Int {
        return userPreferences.first().nightMode
    }
}