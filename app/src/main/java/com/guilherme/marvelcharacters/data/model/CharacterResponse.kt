package com.guilherme.marvelcharacters.data.model

import com.google.gson.annotations.SerializedName

data class CharacterResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("thumbnail") val thumbnail: ImageResponse
) {

    fun toCharacter(): Character {
        return Character(
            id = id,
            name = name,
            description = description,
            thumbnail = Image(
                path = thumbnail.path,
                extension = thumbnail.extension
            )
        )
    }
}

data class ImageResponse(
    @SerializedName("path") val path: String,
    @SerializedName("extension") val extension: String
)