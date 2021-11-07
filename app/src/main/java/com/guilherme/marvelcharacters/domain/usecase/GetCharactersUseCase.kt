package com.guilherme.marvelcharacters.domain.usecase

import com.guilherme.marvelcharacters.domain.model.Character
import com.guilherme.marvelcharacters.domain.repository.CharacterRepository
import kotlinx.coroutines.flow.Flow

class GetCharactersUseCase(
    private val characterRepository: CharacterRepository
) {

    operator fun invoke(name: String): Flow<List<Character>> {
        return characterRepository.getCharacters(name)
    }
}