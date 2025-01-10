package com.guilherme.marvelcharacters.domain.usecase

import com.guilherme.marvelcharacters.data.repository.NightModeRepository

class ToggleDarkModeUseCase(
    private val nightModeRepository: NightModeRepository
) {

    operator fun invoke(isEnabled: Boolean) = nightModeRepository.setDarkModeEnabled(isEnabled)
}