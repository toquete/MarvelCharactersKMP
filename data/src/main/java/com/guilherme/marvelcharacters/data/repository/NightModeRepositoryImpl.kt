package com.guilherme.marvelcharacters.data.repository

import com.guilherme.marvelcharacters.cache.NightModeLocalDataSource

internal class NightModeRepositoryImpl(
    private val nightModeLocalDataSource: NightModeLocalDataSource
) : NightModeRepository {

    override fun isDarkModeEnabled(): Boolean = nightModeLocalDataSource.isDarkModeEnabled()

    override fun setDarkModeEnabled(isEnabled: Boolean) {
        nightModeLocalDataSource.setDarkModeEnabled(isEnabled)
    }

    override fun getDarkMode(): Int = nightModeLocalDataSource.getDarkMode()
}