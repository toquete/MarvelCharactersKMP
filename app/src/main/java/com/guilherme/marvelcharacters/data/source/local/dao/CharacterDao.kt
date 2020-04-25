package com.guilherme.marvelcharacters.data.source.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.guilherme.marvelcharacters.data.model.Character

@Dao
interface CharacterDao {

    @Query("SELECT * FROM character")
    fun getCharacterList(): LiveData<List<Character>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(character: Character)

    @Query("DELETE FROM character")
    suspend fun deleteAll()

    @Delete
    suspend fun delete(characters: List<Character>)
}