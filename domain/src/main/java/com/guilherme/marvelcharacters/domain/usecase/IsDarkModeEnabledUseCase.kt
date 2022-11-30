package com.guilherme.marvelcharacters.domain.usecase

import com.guilherme.marvelcharacters.data.repository.NightModeRepository
import javax.inject.Inject

class IsDarkModeEnabledUseCase @Inject constructor(
    private val nightModeRepository: NightModeRepository
) {

    operator fun invoke(): Boolean = nightModeRepository.isDarkModeEnabled()
}