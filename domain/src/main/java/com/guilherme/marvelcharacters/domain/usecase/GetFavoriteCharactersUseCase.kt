package com.guilherme.marvelcharacters.domain.usecase

import com.guilherme.marvelcharacters.core.model.Character
import com.guilherme.marvelcharacters.data.repository.CharacterRepository
import kotlinx.coroutines.flow.Flow

class GetFavoriteCharactersUseCase(
    private val characterRepository: CharacterRepository
) {

    operator fun invoke(): Flow<List<Character>> = characterRepository.getFavoriteCharacters()
}