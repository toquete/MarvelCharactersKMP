package com.guilherme.marvelcharacters.data.repository

import androidx.lifecycle.LiveData
import com.guilherme.marvelcharacters.data.model.Character
import com.guilherme.marvelcharacters.data.source.local.dao.CharacterDao
import com.guilherme.marvelcharacters.data.source.remote.CharacterRemoteDataSource

class CharacterRepository(
    private val remoteDataSource: CharacterRemoteDataSource,
    private val characterDao: CharacterDao
) {

    suspend fun getCharacters(name: String): List<Character> {
        return remoteDataSource.getCharacters(name)
            .map { it.toCharacter() }
    }

    fun isCharacterFavorite(id: Int): LiveData<Boolean> = characterDao.isCharacterFavorite(id)

    fun getFavoriteCharacters(): LiveData<List<Character>> = characterDao.getCharacterList()

    suspend fun insertFavoriteCharacter(character: Character) = characterDao.insert(character)

    suspend fun deleteFavoriteCharacter(character: Character) = characterDao.delete(character)

    suspend fun deleteAllFavoriteCharacters() = characterDao.deleteAll()
}