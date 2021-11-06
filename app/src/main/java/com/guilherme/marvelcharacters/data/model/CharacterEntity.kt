package com.guilherme.marvelcharacters.data.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.guilherme.marvelcharacters.domain.model.Character
import com.guilherme.marvelcharacters.domain.model.Image

@Entity
data class CharacterEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val description: String,
    @Embedded val thumbnail: ImageEntity
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

data class ImageEntity(val path: String, val extension: String)
