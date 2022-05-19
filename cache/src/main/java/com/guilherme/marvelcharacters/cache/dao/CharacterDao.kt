package com.guilherme.marvelcharacters.cache.dao

import androidx.room.Dao
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

    @Query("SELECT * FROM characterentity WHERE id = :id")
    fun getFavoriteCharacter(id: Int): Flow<CharacterEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(character: CharacterEntity)

    @Query("DELETE FROM characterentity")
    suspend fun deleteAll()

    @Query("DELETE FROM characterentity WHERE id = :id")
    suspend fun delete(id: Int)
}