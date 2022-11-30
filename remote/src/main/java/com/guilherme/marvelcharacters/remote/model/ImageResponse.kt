package com.guilherme.marvelcharacters.remote.model

import com.guilherme.marvelcharacters.core.model.Image
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ImageResponse(
    @SerialName("path") val path: String,
    @SerialName("extension") val extension: String
)

fun ImageResponse.toExternalModel() = Image(
    path = path,
    extension = extension
)