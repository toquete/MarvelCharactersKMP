package com.guilherme.marvelcharacters.domain.usecase

import com.guilherme.marvelcharacters.data.repository.CharacterRepository
import com.guilherme.marvelcharacters.domain.model.FavoriteCharacter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class GetFavoriteCharacterByIdUseCase @Inject constructor(
    private val repository: CharacterRepository
) {

    suspend operator fun invoke(id: Int, key: String, privateKey: String): Flow<FavoriteCharacter> {
        return combine(
            flowOf(repository.getCharacterById(id, key, privateKey)),
            repository.isCharacterFavorite(id),
        ) { character, isFavorite ->
            FavoriteCharacter(
                character = character,
                isFavorite = isFavorite
            )
        }
    }
}