package com.guilherme.marvelcharacters.remote.model

import com.guilherme.marvelcharacters.core.model.Character
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class CharacterResponse(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
    @SerialName("description") val description: String,
    @SerialName("thumbnail") val thumbnail: ImageResponse
)

internal fun CharacterResponse.toExternalModel() = Character(
    id = id,
    name = name,
    description = description,
    thumbnail = thumbnail.toExternalModel()
)