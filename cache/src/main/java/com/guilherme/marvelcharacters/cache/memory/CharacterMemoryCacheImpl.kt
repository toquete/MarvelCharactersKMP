package com.guilherme.marvelcharacters.cache.memory

import com.guilherme.marvelcharacters.data.model.CharacterData
import javax.inject.Inject

class CharacterMemoryCacheImpl @Inject constructor(
    private val cache: MutableList<CharacterData> = mutableListOf()
) : CharacterMemoryCache {

    override fun setCache(list: List<CharacterData>) {
        cache.clear()
        cache.addAll(list)
    }

    override fun getCharacter(id: Int): CharacterData {
        return cache.first { it.id == id }
    }
}