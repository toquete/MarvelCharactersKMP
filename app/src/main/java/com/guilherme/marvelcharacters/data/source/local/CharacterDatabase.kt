package com.guilherme.marvelcharacters.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.guilherme.marvelcharacters.data.model.Character
import com.guilherme.marvelcharacters.data.source.local.dao.CharacterDao

@Database(entities = [Character::class], version = 1)
abstract class CharacterDatabase : RoomDatabase() {

    abstract fun characterDao(): CharacterDao
}