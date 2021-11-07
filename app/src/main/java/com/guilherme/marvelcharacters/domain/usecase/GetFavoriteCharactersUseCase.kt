package com.guilherme.marvelcharacters.domain.usecase

import com.guilherme.marvelcharacters.domain.model.Character
import com.guilherme.marvelcharacters.domain.repository.CharacterRepository
import kotlinx.coroutines.flow.Flow

class GetFavoriteCharactersUseCase(
    private val characterRepository: CharacterRepository
) {

    operator fun invoke(): Flow<List<Character>> = characterRepository.getFavoriteCharacters()
}