package com.guilherme.marvelcharacters.cache.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.guilherme.marvelcharacters.cache.model.CharacterEntity

@Dao
internal interface CharacterDao {

    @Query("SELECT * FROM characterentity WHERE name LIKE :name || '%'")
    suspend fun getCharactersByName(name: String): List<CharacterEntity>

    @Query("SELECT * FROM characterentity WHERE id = :id")
    suspend fun getCharacterById(id: Int): CharacterEntity?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(characters: List<CharacterEntity>)
}