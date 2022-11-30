package com.guilherme.marvelcharacters.cache.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import com.guilherme.marvelcharacters.cache.model.CharacterEntity

@Database(entities = [CharacterEntity::class], version = 1)
internal abstract class CharacterDatabase : RoomDatabase() {

    abstract fun characterDao(): CharacterDao
}