package com.guilherme.marvelcharacters.data.model

data class CharacterData(
    val id: Int,
    val name: String,
    val description: String,
    val thumbnail: ImageData
)

data class ImageData(val path: String, val extension: String)