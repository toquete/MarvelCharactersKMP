package com.guilherme.marvelcharacters.mapper

import com.guilherme.marvelcharacters.domain.model.Character
import com.guilherme.marvelcharacters.model.CharacterVO
import javax.inject.Inject

class CharacterMapper @Inject constructor() : Mapper<Character, CharacterVO> {

    override fun mapTo(source: Character): CharacterVO {
        return CharacterVO(
            id = source.id,
            name = source.name,
            description = source.description,
            thumbnail = source.thumbnail
        )
    }

    override fun mapFrom(origin: CharacterVO): Character {
        return Character(
            id = origin.id,
            name = origin.name,
            description = origin.description,
            thumbnail = origin.thumbnail
        )
    }
}