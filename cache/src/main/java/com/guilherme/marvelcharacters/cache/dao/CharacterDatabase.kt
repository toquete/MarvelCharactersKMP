package com.guilherme.marvelcharacters.cache.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import com.guilherme.marvelcharacters.cache.model.CharacterEntity
import com.guilherme.marvelcharacters.cache.model.FavoriteCharacterEntity

@Database(entities = [CharacterEntity::class, FavoriteCharacterEntity::class], version = 1)
internal abstract class CharacterDatabase : RoomDatabase() {

    abstract fun characterDao(): CharacterDao

    abstract fun favoriteCharacterDao(): FavoriteCharacterDao
}