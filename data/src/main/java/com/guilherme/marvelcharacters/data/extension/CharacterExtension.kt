package com.guilherme.marvelcharacters.data.extension

import com.guilherme.marvelcharacters.cache.model.CharacterEntity
import com.guilherme.marvelcharacters.core.model.Character

fun Character.toEntity() = CharacterEntity(
    id = id,
    name = name,
    description = description,
    thumbnail = thumbnail.toEntity()
)