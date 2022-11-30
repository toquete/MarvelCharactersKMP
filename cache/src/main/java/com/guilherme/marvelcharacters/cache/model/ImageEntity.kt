package com.guilherme.marvelcharacters.cache.model

import com.guilherme.marvelcharacters.core.model.Image

internal data class ImageEntity(val path: String, val extension: String)

internal fun ImageEntity.toExternalModel() = Image(
    path = path,
    extension = extension
)