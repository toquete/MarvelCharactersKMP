package com.guilherme.marvelcharacters.data.extension

import com.guilherme.marvelcharacters.cache.model.ImageEntity
import com.guilherme.marvelcharacters.core.model.Image

fun Image.toEntity() = ImageEntity(
    path = path,
    extension = extension
)