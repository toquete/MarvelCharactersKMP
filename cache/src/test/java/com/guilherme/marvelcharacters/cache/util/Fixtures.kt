package com.guilherme.marvelcharacters.cache.util

import com.guilherme.marvelcharacters.cache.model.CharacterEntity
import com.guilherme.marvelcharacters.cache.model.FavoriteCharacterEntity
import com.guilherme.marvelcharacters.core.model.Character

internal object Fixtures {

    val character = Character(
        id = 0,
        name = "Spider-Man",
        description = "",
        thumbnail = "test.jpg"
    )

    val characterEntity = CharacterEntity(
        id = 0,
        name = "Spider-Man",
        description = "",
        thumbnail = "test.jpg"
    )

    val favoriteCharacterEntity = FavoriteCharacterEntity(
        id = 0,
        name = "Spider-Man",
        description = "",
        thumbnail = "test.jpg"
    )

    val characterList = listOf(character)

    val characterEntityList = listOf(characterEntity)

    val favoriteCharacterEntityList = listOf(favoriteCharacterEntity)
}