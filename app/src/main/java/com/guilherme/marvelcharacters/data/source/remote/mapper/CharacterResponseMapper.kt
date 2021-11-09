package com.guilherme.marvelcharacters.data.source.remote.mapper

import com.guilherme.marvelcharacters.data.model.CharacterData
import com.guilherme.marvelcharacters.data.model.ImageData
import com.guilherme.marvelcharacters.data.source.remote.model.CharacterResponse
import com.guilherme.marvelcharacters.infrastructure.util.Mapper
import javax.inject.Inject

class CharacterResponseMapper @Inject constructor() : Mapper<CharacterResponse, CharacterData> {

    override fun mapTo(source: CharacterResponse): CharacterData {
        return CharacterData(
            id = source.id,
            name = source.name,
            description = source.description,
            thumbnail = ImageData(
                path = source.thumbnail.path,
                extension = source.thumbnail.extension
            )
        )
    }

    override fun mapFrom(origin: CharacterData): CharacterResponse {
        throw UnsupportedOperationException()
    }
}