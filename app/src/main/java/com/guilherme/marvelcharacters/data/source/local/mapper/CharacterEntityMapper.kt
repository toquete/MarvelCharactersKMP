package com.guilherme.marvelcharacters.data.source.local.mapper

import com.guilherme.marvelcharacters.data.model.CharacterEntity
import com.guilherme.marvelcharacters.data.model.ImageEntity
import com.guilherme.marvelcharacters.domain.model.Character
import com.guilherme.marvelcharacters.domain.model.Image
import com.guilherme.marvelcharacters.infrastructure.util.Mapper

class CharacterEntityMapper : Mapper<CharacterEntity, Character> {

    override fun mapTo(source: CharacterEntity): Character {
        return Character(
            id = source.id,
            name = source.name,
            description = source.description,
            thumbnail = Image(
                path = source.thumbnail.path,
                extension = source.thumbnail.extension
            )
        )
    }

    override fun mapFrom(origin: Character): CharacterEntity {
        return CharacterEntity(
            id = origin.id,
            name = origin.name,
            description = origin.description,
            thumbnail = ImageEntity(
                path = origin.thumbnail.path,
                extension = origin.thumbnail.extension
            )
        )
    }
}