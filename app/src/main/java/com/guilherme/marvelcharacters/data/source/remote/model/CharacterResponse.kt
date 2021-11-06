package com.guilherme.marvelcharacters.data.source.remote.model

import com.google.gson.annotations.SerializedName

data class CharacterResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("thumbnail") val thumbnail: ImageResponse
)

data class ImageResponse(
    @SerializedName("path") val path: String,
    @SerializedName("extension") val extension: String
)