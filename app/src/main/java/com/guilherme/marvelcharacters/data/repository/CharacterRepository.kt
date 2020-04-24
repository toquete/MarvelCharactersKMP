package com.guilherme.marvelcharacters.data.repository

import androidx.lifecycle.LiveData
import com.guilherme.marvelcharacters.BuildConfig
import com.guilherme.marvelcharacters.data.model.Character
import com.guilherme.marvelcharacters.data.source.local.dao.CharacterDao
import com.guilherme.marvelcharacters.data.source.remote.Api
import kotlinx.coroutines.withContext
import org.apache.commons.codec.binary.Hex
import org.apache.commons.codec.digest.DigestUtils
import kotlin.coroutines.CoroutineContext

class CharacterRepository(
    private val api: Api,
    private val characterDao: CharacterDao,
    private val coroutineContext: CoroutineContext
) {

    suspend fun getCharacters(name: String): List<Character> {
        val ts = System.currentTimeMillis().toString()
        val hash = String(Hex.encodeHex(DigestUtils.md5(ts + BuildConfig.MARVEL_PRIVATE_KEY + BuildConfig.MARVEL_KEY)))

        return api.getCharacters(ts, hash, BuildConfig.MARVEL_KEY, name)
            .container
            .characters
    }

    fun getFavoriteCharacters(): LiveData<List<Character>> = characterDao.getCharacterList()

    suspend fun insertFavoriteCharacters(characters: List<Character>) = withContext(coroutineContext) { characterDao.insert(characters) }

    suspend fun deleteFavoriteCharacter(character: Character) = withContext(coroutineContext) { characterDao.delete(character) }

    suspend fun deleteAllFavoriteCharacter() = withContext(coroutineContext) { characterDao.deleteAll() }
}