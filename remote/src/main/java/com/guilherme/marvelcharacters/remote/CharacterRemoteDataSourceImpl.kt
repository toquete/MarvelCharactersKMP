package com.guilherme.marvelcharacters.remote

import com.guilherme.marvelcharacters.data.model.CharacterData
import com.guilherme.marvelcharacters.data.model.ImageData
import com.guilherme.marvelcharacters.data.source.remote.CharacterRemoteDataSource
import com.guilherme.marvelcharacters.remote.service.Api
import org.apache.commons.codec.binary.Hex
import org.apache.commons.codec.digest.DigestUtils
import javax.inject.Inject

class CharacterRemoteDataSourceImpl @Inject constructor(
    private val api: Api
) : CharacterRemoteDataSource {

    override suspend fun getCharacters(
        name: String,
        key: String,
        privateKey: String
    ): List<CharacterData> {
        // TODO: mover regra para use case
        val ts = System.currentTimeMillis().toString()
        val hash = String(Hex.encodeHex(DigestUtils.md5(ts + privateKey + key)))

        return api.getCharacters(ts, hash, key, name)
            .container
            .results
            .map { source ->
                CharacterData(
                    id = source.id,
                    name = source.name,
                    description = source.description,
                    thumbnail = ImageData(
                        path = source.thumbnail.path,
                        extension = source.thumbnail.extension
                    )
                )
            }
    }
}