package com.guilherme.marvelcharacters.domain.usecase

import com.guilherme.marvelcharacters.core.model.Character
import com.guilherme.marvelcharacters.data.repository.CharacterRepository
import javax.inject.Inject

class GetCharacterByIdUseCase @Inject constructor(
    private val repository: CharacterRepository
) {

    suspend operator fun invoke(id: Int, key: String, privateKey: String): Character {
        return repository.getCharacterById(id, key, privateKey)
    }
}