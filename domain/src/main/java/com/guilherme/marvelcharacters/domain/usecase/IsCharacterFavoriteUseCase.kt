package com.guilherme.marvelcharacters.domain.usecase

import com.guilherme.marvelcharacters.data.repository.CharacterRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class IsCharacterFavoriteUseCase @Inject constructor(
    private val characterRepository: CharacterRepository
) {

    operator fun invoke(id: Int): Flow<Boolean> = characterRepository.isCharacterFavorite(id)
}