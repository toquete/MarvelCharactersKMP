package com.guilherme.marvelcharacters.data.source.local

interface NightModeLocalDataSource {

    fun isDarkModeEnabled(): Boolean

    fun setDarkModeEnabled(isEnabled: Boolean)

    fun getDarkMode(): Int
}