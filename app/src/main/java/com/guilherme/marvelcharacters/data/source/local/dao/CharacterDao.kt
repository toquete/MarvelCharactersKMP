package com.guilherme.marvelcharacters.data.source.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.guilherme.marvelcharacters.data.model.CharacterEntity

@Dao
interface CharacterDao {

    @Query("SELECT * FROM characterentity")
    suspend fun getCharacterList(): List<CharacterEntity>

    @Query("SELECT COUNT(*) FROM characterentity WHERE id = :id")
    suspend fun isCharacterFavorite(id: Int): Boolean

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(character: CharacterEntity)

    @Query("DELETE FROM characterentity")
    suspend fun deleteAll()

    @Delete
    suspend fun delete(character: CharacterEntity)
}