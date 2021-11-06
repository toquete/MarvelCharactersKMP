package com.guilherme.marvelcharacters.domain.usecase

import com.guilherme.marvelcharacters.domain.model.Character
import com.guilherme.marvelcharacters.domain.repository.CharacterRepository

class GetCharactersUseCase(
    private val characterRepository: CharacterRepository
) {

    suspend operator fun invoke(name: String): List<Character> {
        return characterRepository.getCharacters(name)
    }
}