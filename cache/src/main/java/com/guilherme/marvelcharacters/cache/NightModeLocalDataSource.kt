package com.guilherme.marvelcharacters.cache

interface NightModeLocalDataSource {

    fun isDarkModeEnabled(): Boolean

    fun setDarkModeEnabled(isEnabled: Boolean)

    fun getDarkMode(): Int
}