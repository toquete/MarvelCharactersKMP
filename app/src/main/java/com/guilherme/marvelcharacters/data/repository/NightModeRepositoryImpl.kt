package com.guilherme.marvelcharacters.data.repository

import com.guilherme.marvelcharacters.data.source.local.NightModeLocalDataSource
import com.guilherme.marvelcharacters.domain.repository.NightModeRepository

class NightModeRepositoryImpl(
    private val nightModeLocalDataSource: NightModeLocalDataSource
) : NightModeRepository {

    override fun isDarkModeEnabled(): Boolean = nightModeLocalDataSource.isDarkModeEnabled()

    override fun setDarkModeEnabled(isEnabled: Boolean) {
        nightModeLocalDataSource.setDarkModeEnabled(isEnabled)
    }

    override fun getDarkMode(): Int = nightModeLocalDataSource.getDarkMode()

}