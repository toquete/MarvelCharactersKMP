package com.guilherme.marvelcharacters.data.repository

interface NightModeRepository {

    suspend fun isDarkModeEnabled(): Boolean

    suspend fun setDarkModeEnabled(isEnabled: Boolean)

    suspend fun getDarkMode(): Int
}