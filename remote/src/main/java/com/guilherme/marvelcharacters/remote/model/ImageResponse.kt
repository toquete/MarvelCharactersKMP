package com.guilherme.marvelcharacters.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class ImageResponse(
    @SerialName("path") val path: String,
    @SerialName("extension") val extension: String
)