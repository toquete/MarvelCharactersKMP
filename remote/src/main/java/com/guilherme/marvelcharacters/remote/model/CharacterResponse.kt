package com.guilherme.marvelcharacters.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CharacterResponse(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
    @SerialName("description") val description: String,
    @SerialName("thumbnail") val thumbnail: ImageResponse
)

@Serializable
data class ImageResponse(
    @SerialName("path") val path: String,
    @SerialName("extension") val extension: String
)