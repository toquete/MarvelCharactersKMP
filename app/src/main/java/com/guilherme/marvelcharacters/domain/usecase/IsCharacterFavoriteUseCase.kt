package com.guilherme.marvelcharacters.domain.usecase

import com.guilherme.marvelcharacters.domain.repository.CharacterRepository
import kotlinx.coroutines.flow.Flow

class IsCharacterFavoriteUseCase(
    private val characterRepository: CharacterRepository
) {

    operator fun invoke(id: Int): Flow<Boolean> = characterRepository.isCharacterFavorite(id)
}