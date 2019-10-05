package com.guilherme.marvelcharacters.data.repository

import com.guilherme.marvelcharacters.BuildConfig
import com.guilherme.marvelcharacters.data.source.remote.Api
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.apache.commons.codec.binary.Hex
import org.apache.commons.codec.digest.DigestUtils

class CharacterRepositoryImpl(private val api: Api) : CharacterRepository {

    override suspend fun getCharacters(name: String) = withContext(Dispatchers.IO) {
        val ts = System.currentTimeMillis().toString()
        val hash = String(Hex.encodeHex(DigestUtils.md5(ts + BuildConfig.MARVEL_PRIVATE_KEY + BuildConfig.MARVEL_KEY)))

        api.getCharacters(ts, hash, BuildConfig.MARVEL_KEY, name)
            .container
            .characters
    }
}