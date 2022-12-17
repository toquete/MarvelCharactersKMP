package com.guilherme.marvelcharacters.cache.dao

import androidx.room.Dao
import androidx.room.Query
import com.guilherme.marvelcharacters.cache.model.FavoriteCharacterEntity
import kotlinx.coroutines.flow.Flow

@Dao
internal interface FavoriteCharacterDao {

    @Query("SELECT * FROM favoritecharacterentity")
    fun getFavoriteCharacters(): Flow<List<FavoriteCharacterEntity>>

    @Query("SELECT COUNT(*) FROM favoritecharacterentity WHERE id = :id")
    fun isCharacterFavorite(id: Int): Flow<Boolean>

    @Query(
        "INSERT INTO favoritecharacterentity " +
            "SELECT * FROM characterentity " +
            "WHERE id = :id"
    )
    suspend fun copyFavoriteCharacter(id: Int)

    @Query("DELETE FROM favoritecharacterentity")
    suspend fun deleteAll()

    @Query("DELETE FROM favoritecharacterentity WHERE id = :id")
    suspend fun delete(id: Int)
}