package com.guilherme.marvelcharacters.cache.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.guilherme.marvelcharacters.cache.model.CharacterEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CharacterDao {

    @Query("SELECT * FROM characterentity")
    fun getCharacterList(): Flow<List<CharacterEntity>>

    @Query("SELECT COUNT(*) FROM characterentity WHERE id = :id")
    fun isCharacterFavorite(id: Int): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(character: CharacterEntity)

    @Query("DELETE FROM characterentity")
    suspend fun deleteAll()

    @Delete
    suspend fun delete(character: CharacterEntity)
}