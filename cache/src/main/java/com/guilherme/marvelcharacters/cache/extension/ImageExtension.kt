package com.guilherme.marvelcharacters.cache.extension

import com.guilherme.marvelcharacters.cache.model.ImageEntity
import com.guilherme.marvelcharacters.core.model.Image

internal fun Image.toEntity() = ImageEntity(
    path = path,
    extension = extension
)