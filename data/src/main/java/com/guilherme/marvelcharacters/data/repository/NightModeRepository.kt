package com.guilherme.marvelcharacters.data.repository

interface NightModeRepository {

    fun isDarkModeEnabled(): Boolean

    fun setDarkModeEnabled(isEnabled: Boolean)

    fun getDarkMode(): Int
}