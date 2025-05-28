package com.guilherme.marvelcharacters.data.repository

import com.guilherme.marvelcharacters.cache.NightModeLocalDataSource

internal class NightModeRepositoryImpl(
    private val nightModeLocalDataSource: NightModeLocalDataSource
) : NightModeRepository {

    override suspend fun isDarkModeEnabled(): Boolean = nightModeLocalDataSource.isDarkModeEnabled()

    override suspend fun setDarkModeEnabled(isEnabled: Boolean) {
        nightModeLocalDataSource.setDarkModeEnabled(isEnabled)
    }

    override suspend fun getDarkMode(): Int = nightModeLocalDataSource.getDarkMode()
}