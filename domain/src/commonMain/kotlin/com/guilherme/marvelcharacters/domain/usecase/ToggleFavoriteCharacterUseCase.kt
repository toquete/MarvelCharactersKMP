package com.guilherme.marvelcharacters.domain.usecase

import com.guilherme.marvelcharacters.data.repository.CharacterRepository

class ToggleFavoriteCharacterUseCase(
    private val repository: CharacterRepository
) {

    suspend operator fun invoke(id: Int, isFavorite: Boolean) {
        if (isFavorite) {
            repository.deleteFavoriteCharacter(id)
        } else {
            repository.insertFavoriteCharacter(id)
        }
    }
}