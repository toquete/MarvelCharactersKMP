package com.guilherme.marvelcharacters.cache.memory

import com.google.common.truth.Truth.assertThat
import com.guilherme.marvelcharacters.data.model.CharacterData
import org.junit.Test

class CharacterMemoryCacheImplTest {

    private val list: MutableList<CharacterData> = mutableListOf()

    private val cache = CharacterMemoryCacheImpl(list)

    @Test
    fun `setCache - set cache with list passed as parameter`() {
        val character = CharacterData(
            id = 0,
            name = "Spider-Man",
            description = "",
            thumbnail = ""
        )

        cache.setCache(list = listOf(character))

        assertThat(list).containsExactly(character)
    }

    @Test
    fun `setCache - clean and set cache with list passed as parameter`() {
        list.add(CharacterData(id = 1, name = "Iron Man", description = "", thumbnail = ""))

        val character = CharacterData(
            id = 0,
            name = "Spider-Man",
            description = "",
            thumbnail = ""
        )

        cache.setCache(list = listOf(character))

        assertThat(list).containsExactly(character)
    }

    @Test
    fun `getCharacter - get character by id`() {
        val character = CharacterData(
            id = 0,
            name = "Spider-Man",
            description = "",
            thumbnail = ""
        )
        list.add(character)

        val result = cache.getCharacter(id = 0)

        assertThat(result).isEqualTo(character)
    }
}