package com.guilherme.marvelcharacters.remote

import com.guilherme.marvelcharacters.data.model.CharacterData
import com.guilherme.marvelcharacters.data.source.remote.CharacterRemoteDataSource
import com.guilherme.marvelcharacters.remote.mapper.CharacterResponseMapper
import com.guilherme.marvelcharacters.remote.service.Api
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.apache.commons.codec.binary.Hex
import org.apache.commons.codec.digest.DigestUtils
import javax.inject.Inject

class CharacterRemoteDataSourceImpl @Inject constructor(
    private val api: Api,
    private val mapper: CharacterResponseMapper
) : CharacterRemoteDataSource {

    override fun getCharacters(
        name: String,
        key: String,
        privateKey: String
    ): Flow<List<CharacterData>> = flow {
        // TODO: mover regra para use case
        val ts = System.currentTimeMillis().toString()
        val hash =
            String(Hex.encodeHex(DigestUtils.md5(ts + privateKey + key)))

        api.getCharacters(ts, hash, key, name)
            .container
            .results
            .map { mapper.mapTo(it) }
            .apply { emit(this) }
    }
}