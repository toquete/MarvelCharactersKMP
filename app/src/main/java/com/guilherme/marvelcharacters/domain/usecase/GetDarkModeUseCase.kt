package com.guilherme.marvelcharacters.domain.usecase

import com.guilherme.marvelcharacters.domain.repository.NightModeRepository

class GetDarkModeUseCase(
    private val nightModeRepository: NightModeRepository
) {

    operator fun invoke(): Int = nightModeRepository.getDarkMode()
}