package com.guilherme.marvelcharacters.data.repository

import com.guilherme.marvelcharacters.data.model.Character
import java.lang.Exception

interface CharacterRepository {

    suspend fun getCharacters(name: String): List<Character>
}

class CharacterRequestException(message: String): Exception(message)