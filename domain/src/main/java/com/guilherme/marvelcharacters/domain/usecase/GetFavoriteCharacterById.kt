package com.guilherme.marvelcharacters.domain.usecase

import com.guilherme.marvelcharacters.domain.model.Character
import com.guilherme.marvelcharacters.domain.repository.CharacterRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavoriteCharacterById @Inject constructor(
    private val characterRepository: CharacterRepository
) {

    operator fun invoke(id: Int): Flow<Character> = characterRepository.getFavoriteCharacter(id)
}