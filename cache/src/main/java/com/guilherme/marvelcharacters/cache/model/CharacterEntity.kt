package com.guilherme.marvelcharacters.cache.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CharacterEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val description: String,
    @Embedded val thumbnail: ImageEntity
)

data class ImageEntity(val path: String, val extension: String)
