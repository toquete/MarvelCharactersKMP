package com.guilherme.marvelcharacters.domain.usecase

import com.guilherme.marvelcharacters.domain.model.Character
import com.guilherme.marvelcharacters.domain.repository.CharacterRepository
import kotlinx.coroutines.flow.Flow

class GetFavoriteCharacterById(
    private val characterRepository: CharacterRepository
) {

    operator fun invoke(id: Int): Flow<Character> = characterRepository.getFavoriteCharacter(id)
}