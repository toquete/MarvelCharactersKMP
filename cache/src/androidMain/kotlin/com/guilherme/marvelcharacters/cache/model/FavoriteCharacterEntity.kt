package com.guilherme.marvelcharacters.cache.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.guilherme.marvelcharacters.core.model.Character

@Entity
internal data class FavoriteCharacterEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val description: String,
    val thumbnail: String
)

internal fun FavoriteCharacterEntity.toExternalModel() = Character(
    id = id,
    name = name,
    description = description,
    thumbnail = thumbnail
)
