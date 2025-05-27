package com.guilherme.marvelcharacters.remote

import com.guilherme.marvelcharacters.core.model.Character
import com.guilherme.marvelcharacters.remote.model.CharacterResponse
import com.guilherme.marvelcharacters.remote.model.toExternalModel
import com.guilherme.marvelcharacters.remote.service.Service
import okio.ByteString.Companion.encodeUtf8

internal class CharacterRemoteDataSourceImpl(
    private val service: Service
) : CharacterRemoteDataSource {

    override suspend fun getCharacters(name: String): List<Character> {
        val (ts, hash) = getKeys()

        return service.getCharacters(ts, hash, BuildKonfig.MARVEL_KEY, name)
            .container
            .results
            .map(CharacterResponse::toExternalModel)
    }

    private fun getKeys(): Pair<String, String> {
        val ts = currentTimestamp()
        val input = ts + BuildKonfig.MARVEL_PRIVATE_KEY + BuildKonfig.MARVEL_KEY
        val hash = input.encodeUtf8().md5().hex()
        return ts to hash
    }
}