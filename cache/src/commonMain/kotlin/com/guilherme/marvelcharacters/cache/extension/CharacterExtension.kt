package com.guilherme.marvelcharacters.cache.extension

import com.guilherme.marvelcharacters.cache.model.CharacterEntity
import com.guilherme.marvelcharacters.core.model.Character

internal fun Character.toEntity() = CharacterEntity(
    id = id,
    name = name,
    description = description,
    thumbnail = thumbnail
)