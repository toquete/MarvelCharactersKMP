package com.guilherme.marvelcharacters.cache.mapper

import com.guilherme.marvelcharacters.cache.model.CharacterEntity
import com.guilherme.marvelcharacters.cache.model.ImageEntity
import com.guilherme.marvelcharacters.data.model.CharacterData
import com.guilherme.marvelcharacters.data.model.ImageData
import javax.inject.Inject

class CharacterEntityMapper @Inject constructor() : Mapper<CharacterEntity, CharacterData> {

    override fun mapTo(source: CharacterEntity): CharacterData {
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

    override fun mapFrom(origin: CharacterData): CharacterEntity {
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