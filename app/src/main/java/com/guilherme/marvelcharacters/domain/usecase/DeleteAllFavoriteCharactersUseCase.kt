package com.guilherme.marvelcharacters.domain.usecase

import com.guilherme.marvelcharacters.domain.repository.CharacterRepository

class DeleteAllFavoriteCharactersUseCase(
    private val characterRepository: CharacterRepository
) {

    suspend operator fun invoke() {
        characterRepository.deleteAllFavoriteCharacters()
    }
}