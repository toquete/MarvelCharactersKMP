package com.guilherme.marvelcharacters.domain.usecase

import com.guilherme.marvelcharacters.domain.model.Character
import com.guilherme.marvelcharacters.domain.repository.CharacterRepository

class DeleteFavoriteCharacterUseCase(
    private val characterRepository: CharacterRepository
) {

    suspend operator fun invoke(character: Character) {
        characterRepository.deleteFavoriteCharacter(character)
    }
}