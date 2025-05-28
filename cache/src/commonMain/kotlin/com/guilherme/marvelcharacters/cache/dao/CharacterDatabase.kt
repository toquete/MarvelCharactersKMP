package com.guilherme.marvelcharacters.cache.dao

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import com.guilherme.marvelcharacters.cache.model.CharacterEntity
import com.guilherme.marvelcharacters.cache.model.FavoriteCharacterEntity

@Suppress("NO_ACTUAL_FOR_EXPECT")
internal expect object CharacterDatabaseConstructor : RoomDatabaseConstructor<CharacterDatabase> {
    override fun initialize(): CharacterDatabase
}

@Database(entities = [CharacterEntity::class, FavoriteCharacterEntity::class], version = 1)
@ConstructedBy(CharacterDatabaseConstructor::class)
internal abstract class CharacterDatabase : RoomDatabase() {

    abstract fun characterDao(): CharacterDao

    abstract fun favoriteCharacterDao(): FavoriteCharacterDao
}