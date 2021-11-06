package com.guilherme.marvelcharacters.data.source.remote

import com.guilherme.marvelcharacters.BuildConfig
import com.guilherme.marvelcharacters.data.model.CharacterData
import com.guilherme.marvelcharacters.data.source.remote.service.Api
import com.guilherme.marvelcharacters.data.source.remote.mapper.CharacterResponseMapper
import org.apache.commons.codec.binary.Hex
import org.apache.commons.codec.digest.DigestUtils

class CharacterRemoteDataSourceImpl(
    private val api: Api,
    private val mapper: CharacterResponseMapper = CharacterResponseMapper()
) : CharacterRemoteDataSource {

    override suspend fun getCharacters(name: String): List<CharacterData> {
        // TODO: mover regra para use case
        val ts = System.currentTimeMillis().toString()
        val hash = String(Hex.encodeHex(DigestUtils.md5(ts + BuildConfig.MARVEL_PRIVATE_KEY + BuildConfig.MARVEL_KEY)))

        return api.getCharacters(ts, hash, BuildConfig.MARVEL_KEY, name)
            .container
            .results
            .map { mapper.mapTo(it) }
    }
}