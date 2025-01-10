package com.guilherme.marvelcharacters.domain.usecase

import com.guilherme.marvelcharacters.data.repository.CharacterRepository
import com.guilherme.marvelcharacters.domain.model.FavoriteCharacter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow

class GetFavoriteCharacterByIdUseCase(
    private val repository: CharacterRepository
) {

    operator fun invoke(id: Int): Flow<FavoriteCharacter> {
        return combine(
            flow { emit(repository.getCharacterById(id)) },
            repository.isCharacterFavorite(id),
        ) { character, isFavorite ->
            FavoriteCharacter(
                character = character,
                isFavorite = isFavorite
            )
        }
    }
}