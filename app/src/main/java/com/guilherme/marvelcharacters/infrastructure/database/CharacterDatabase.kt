package com.guilherme.marvelcharacters.infrastructure.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.guilherme.marvelcharacters.data.model.CharacterEntity
import com.guilherme.marvelcharacters.data.source.local.dao.CharacterDao

@Database(entities = [CharacterEntity::class], version = 1)
abstract class CharacterDatabase : RoomDatabase() {

    abstract fun characterDao(): CharacterDao
}