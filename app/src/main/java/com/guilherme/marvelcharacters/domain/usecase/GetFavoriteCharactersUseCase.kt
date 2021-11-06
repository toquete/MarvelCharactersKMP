package com.guilherme.marvelcharacters.domain.usecase

import com.guilherme.marvelcharacters.domain.model.Character
import com.guilherme.marvelcharacters.domain.repository.CharacterRepository

class GetFavoriteCharactersUseCase(
    private val characterRepository: CharacterRepository
) {

    suspend operator fun invoke(): List<Character> {
        return characterRepository.getFavoriteCharacters()
    }
}