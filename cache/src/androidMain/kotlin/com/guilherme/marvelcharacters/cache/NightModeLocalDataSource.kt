package com.guilherme.marvelcharacters.cache

interface NightModeLocalDataSource {

    suspend fun isDarkModeEnabled(): Boolean

    suspend fun setDarkModeEnabled(isEnabled: Boolean)

    suspend fun getDarkMode(): Int
}