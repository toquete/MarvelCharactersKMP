package com.guilherme.marvelcharacters.domain.usecase

import com.guilherme.marvelcharacters.data.repository.NightModeRepository

class IsDarkModeEnabledUseCase(
    private val nightModeRepository: NightModeRepository
) {

    operator fun invoke(): Boolean = nightModeRepository.isDarkModeEnabled()
}