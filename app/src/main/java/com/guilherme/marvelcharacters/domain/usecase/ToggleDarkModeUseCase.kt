package com.guilherme.marvelcharacters.domain.usecase

import com.guilherme.marvelcharacters.domain.repository.NightModeRepository
import javax.inject.Inject

class ToggleDarkModeUseCase @Inject constructor(
    private val nightModeRepository: NightModeRepository
) {

    operator fun invoke(isEnabled: Boolean) = nightModeRepository.setDarkModeEnabled(isEnabled)
}