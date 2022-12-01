package com.guilherme.marvelcharacters.cache.extension

import com.guilherme.marvelcharacters.cache.model.FavoriteCharacterEntity
import com.guilherme.marvelcharacters.core.model.Character

internal fun Character.toFavoriteEntity() = FavoriteCharacterEntity(
    id = id,
    name = name,
    description = description,
    thumbnail = thumbnail.toEntity()
)