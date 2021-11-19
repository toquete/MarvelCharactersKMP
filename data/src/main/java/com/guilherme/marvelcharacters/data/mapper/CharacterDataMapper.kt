package com.guilherme.marvelcharacters.data.mapper

import com.guilherme.marvelcharacters.data.model.CharacterData
import com.guilherme.marvelcharacters.data.model.ImageData
import com.guilherme.marvelcharacters.domain.model.Character
import com.guilherme.marvelcharacters.domain.model.Image
import javax.inject.Inject

class CharacterDataMapper @Inject constructor() : Mapper<CharacterData, Character> {

    override fun mapTo(source: CharacterData): Character {
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

    override fun mapFrom(origin: Character): CharacterData {
        return CharacterData(
            id = origin.id,
            name = origin.name,
            description = origin.description,
            thumbnail = ImageData(
                path = origin.thumbnail.path,
                extension = origin.thumbnail.extension
            )
        )
    }
}