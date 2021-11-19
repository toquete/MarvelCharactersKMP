package com.guilherme.marvelcharacters.domain.usecase

import com.guilherme.marvelcharacters.domain.model.Character
import com.guilherme.marvelcharacters.domain.repository.CharacterRepository
import javax.inject.Inject

class InsertFavoriteCharacterUseCase @Inject constructor(
    private val characterRepository: CharacterRepository
) {

    suspend operator fun invoke(character: Character) {
        characterRepository.insertFavoriteCharacter(character)
    }
}