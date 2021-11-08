package com.guilherme.marvelcharacters.domain.repository

interface NightModeRepository {

    fun isDarkModeEnabled(): Boolean

    fun setDarkModeEnabled(isEnabled: Boolean)

    fun getDarkMode(): Int
}