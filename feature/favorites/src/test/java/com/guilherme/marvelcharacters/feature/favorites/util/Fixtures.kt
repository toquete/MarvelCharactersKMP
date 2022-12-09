package com.guilherme.marvelcharacters.feature.favorites.util

import com.guilherme.marvelcharacters.core.model.Character

internal object Fixtures {

    val character = Character(
        id = 0,
        name = "Spider-Man",
        description = "",
        thumbnail = "test.jpg"
    )

    val characterList = listOf(character)
}