package com.guilherme.marvelcharacters.domain.usecase

import com.guilherme.marvelcharacters.core.model.Character
import com.guilherme.marvelcharacters.data.repository.CharacterRepository
import javax.inject.Inject

class DeleteFavoriteCharacterUseCase @Inject constructor(
    private val characterRepository: CharacterRepository
) {

    suspend operator fun invoke(character: Character) {
        characterRepository.deleteFavoriteCharacter(character)
    }
}