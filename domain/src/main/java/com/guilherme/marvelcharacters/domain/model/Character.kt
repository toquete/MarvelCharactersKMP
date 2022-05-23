package com.guilherme.marvelcharacters.domain.model

data class Character(
    val id: Int,
    val name: String,
    val description: String,
    val thumbnail: String,
    val isFavorite: Boolean = false
)
