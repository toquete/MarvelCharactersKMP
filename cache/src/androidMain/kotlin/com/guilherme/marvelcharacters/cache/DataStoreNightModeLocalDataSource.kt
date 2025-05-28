package com.guilherme.marvelcharacters.cache

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.guilherme.marvelcharacters.cache.PreferencesKeys.DARK_THEME_CONFIG
import com.guilherme.marvelcharacters.core.model.DarkThemeConfig
import com.guilherme.marvelcharacters.core.model.UserPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

internal class DataStoreNightModeLocalDataSource(
    private val dataStore: DataStore<Preferences>
) : NightModeLocalDataSource {

    private val userPreferences: Flow<UserPreferences> = dataStore.data.map { preferences ->
        val darkThemeConfig = preferences[DARK_THEME_CONFIG]?.let {
            DarkThemeConfig.entries[it]
        } ?: DarkThemeConfig.FOLLOW_SYSTEM

        UserPreferences(darkThemeConfig)
    }

    override suspend fun isDarkModeEnabled(): Boolean {
        return userPreferences.firstOrNull()?.darkThemeConfig == DarkThemeConfig.DARK
    }

    override suspend fun setDarkModeEnabled(isEnabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[DARK_THEME_CONFIG] = if (isEnabled) {
                DarkThemeConfig.DARK.ordinal
            } else {
                DarkThemeConfig.LIGHT.ordinal
            }
        }
    }

    override suspend fun getDarkMode(): Int {
        return userPreferences.first().darkThemeConfig.ordinal
    }
}