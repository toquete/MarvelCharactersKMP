package com.guilherme.marvelcharacters.data.source.remote

import com.guilherme.marvelcharacters.BuildConfig
import com.guilherme.marvelcharacters.data.model.CharacterData
import com.guilherme.marvelcharacters.data.source.remote.mapper.CharacterResponseMapper
import com.guilherme.marvelcharacters.data.source.remote.service.Api
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.apache.commons.codec.binary.Hex
import org.apache.commons.codec.digest.DigestUtils

class CharacterRemoteDataSourceImpl(
    private val api: Api,
    private val mapper: CharacterResponseMapper = CharacterResponseMapper()
) : CharacterRemoteDataSource {

    override fun getCharacters(name: String): Flow<List<CharacterData>> = flow {
        // TODO: mover regra para use case
        val ts = System.currentTimeMillis().toString()
        val hash =
            String(Hex.encodeHex(DigestUtils.md5(ts + BuildConfig.MARVEL_PRIVATE_KEY + BuildConfig.MARVEL_KEY)))

        api.getCharacters(ts, hash, BuildConfig.MARVEL_KEY, name)
            .container
            .results
            .map { mapper.mapTo(it) }
            .apply { emit(this) }
    }
}