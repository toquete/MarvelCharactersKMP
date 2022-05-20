package com.guilherme.marvelcharacters.cache.memory

import com.guilherme.marvelcharacters.data.model.CharacterData

interface CharacterMemoryCache {

    fun setCache(list: List<CharacterData>)

    fun getCharacter(id: Int): CharacterData
}