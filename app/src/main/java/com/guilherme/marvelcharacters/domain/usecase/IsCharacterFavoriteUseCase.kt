package com.guilherme.marvelcharacters.domain.usecase

import com.guilherme.marvelcharacters.domain.repository.CharacterRepository

class IsCharacterFavoriteUseCase(
    private val characterRepository: CharacterRepository
) {

    suspend operator fun invoke(id: Int): Boolean = characterRepository.isCharacterFavorite(id)
}