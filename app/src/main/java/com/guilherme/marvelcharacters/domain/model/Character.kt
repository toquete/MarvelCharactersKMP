package com.guilherme.marvelcharacters.domain.model

import com.guilherme.marvelcharacters.ui.model.CharacterVO
import com.guilherme.marvelcharacters.ui.model.ImageVO

data class Character(
    val id: Int,
    val name: String,
    val description: String,
    val thumbnail: Image
) {

    fun toCharacterVO(): CharacterVO {
        return CharacterVO(
            id,
            name,
            description,
            thumbnail = ImageVO(
                path = thumbnail.path,
                extension = thumbnail.extension
            )
        )
    }
}

data class Image(val path: String, val extension: String)
