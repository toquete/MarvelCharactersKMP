package com.guilherme.marvelcharacters.remote

import com.guilherme.marvelcharacters.core.model.Character
import com.guilherme.marvelcharacters.remote.model.CharacterResponse
import com.guilherme.marvelcharacters.remote.model.toExternalModel
import com.guilherme.marvelcharacters.remote.service.Api
import org.apache.commons.codec.binary.Hex
import org.apache.commons.codec.digest.DigestUtils

internal class CharacterRemoteDataSourceImpl(
    private val api: Api
) : CharacterRemoteDataSource {

    override suspend fun getCharacters(name: String): List<Character> {
        val (ts, hash) = getKeys()

        return api.getCharacters(ts, hash, BuildKonfig.MARVEL_KEY, name)
            .container
            .results
            .map(CharacterResponse::toExternalModel)
    }

    private fun getKeys(): Pair<String, String> {
        val ts = System.currentTimeMillis().toString()
        val hash = String(Hex.encodeHex(DigestUtils.md5(ts + BuildKonfig.MARVEL_PRIVATE_KEY + BuildKonfig.MARVEL_KEY)))
        return Pair(ts, hash)
    }
}