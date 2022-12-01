package com.guilherme.marvelcharacters.cache.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.guilherme.marvelcharacters.cache.model.CharacterEntity
import kotlinx.coroutines.flow.Flow

@Dao
internal interface CharacterDao {

    @Query("SELECT * FROM characterentity")
    fun getCharacterList(): Flow<List<CharacterEntity>>

    @Query("SELECT * FROM characterentity WHERE name LIKE :name || '%'")
    suspend fun getCharactersByName(name: String): List<CharacterEntity>

    @Query("SELECT * FROM characterentity WHERE id = :id")
    suspend fun getCharacterById(id: Int): CharacterEntity?

    @Query("SELECT COUNT(*) FROM characterentity WHERE id = :id")
    fun isCharacterFavorite(id: Int): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(character: CharacterEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(characters: List<CharacterEntity>)

    @Query("DELETE FROM characterentity")
    suspend fun deleteAll()

    @Delete
    suspend fun delete(character: CharacterEntity)
}