package com.guilherme.marvelcharacters.cache.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.guilherme.marvelcharacters.core.model.Character

@Entity
data class CharacterEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val description: String,
    @Embedded val thumbnail: ImageEntity
)

fun CharacterEntity.toExternalModel() = Character(
    id = id,
    name = name,
    description = description,
    thumbnail = thumbnail.toExternalModel()
)
