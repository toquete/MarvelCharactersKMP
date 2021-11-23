package com.guilherme.marvelcharacters.mapper

import com.guilherme.marvelcharacters.domain.model.Character
import com.guilherme.marvelcharacters.domain.model.Image
import com.guilherme.marvelcharacters.model.CharacterVO
import com.guilherme.marvelcharacters.model.ImageVO
import javax.inject.Inject

class CharacterMapper @Inject constructor() : Mapper<Character, CharacterVO> {

    override fun mapTo(source: Character): CharacterVO {
        return CharacterVO(
            id = source.id,
            name = source.name,
            description = source.description,
            thumbnail = ImageVO(
                path = source.thumbnail.path,
                extension = source.thumbnail.extension
            )
        )
    }

    override fun mapFrom(origin: CharacterVO): Character {
        return Character(
            id = origin.id,
            name = origin.name,
            description = origin.description,
            thumbnail = Image(
                path = origin.thumbnail.path,
                extension = origin.thumbnail.extension
            )
        )
    }
}