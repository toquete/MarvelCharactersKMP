package com.guilherme.marvelcharacters.cache.model

import com.guilherme.marvelcharacters.core.model.Image

data class ImageEntity(val path: String, val extension: String)

fun ImageEntity.toExternalModel() = Image(
    path = path,
    extension = extension
)