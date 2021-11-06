package com.guilherme.marvelcharacters.data.source.remote.mapper

import com.guilherme.marvelcharacters.data.source.remote.model.CharacterResponse
import com.guilherme.marvelcharacters.domain.model.Character
import com.guilherme.marvelcharacters.domain.model.Image
import com.guilherme.marvelcharacters.infrastructure.util.Mapper

class CharacterResponseMapper : Mapper<CharacterResponse, Character> {

    override fun mapTo(source: CharacterResponse): Character {
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

    override fun mapFrom(origin: Character): CharacterResponse {
        throw UnsupportedOperationException()
    }
}