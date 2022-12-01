package com.guilherme.marvelcharacters.cache.dao

import androidx.room.*
import com.guilherme.marvelcharacters.cache.model.FavoriteCharacterEntity
import kotlinx.coroutines.flow.Flow

@Dao
internal interface FavoriteCharacterDao {

    @Query("SELECT * FROM favoritecharacterentity")
    fun getFavoriteCharacters(): Flow<List<FavoriteCharacterEntity>>

    @Query("SELECT COUNT(*) FROM favoritecharacterentity WHERE id = :id")
    fun isCharacterFavorite(id: Int): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(characters: List<FavoriteCharacterEntity>)

    @Query("DELETE FROM favoritecharacterentity")
    suspend fun deleteAll()

    @Delete
    suspend fun delete(character: FavoriteCharacterEntity)
}