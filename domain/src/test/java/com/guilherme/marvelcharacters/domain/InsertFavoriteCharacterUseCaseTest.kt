package com.guilherme.marvelcharacters.domain

import com.guilherme.marvelcharacters.domain.model.Character
import com.guilherme.marvelcharacters.domain.model.Image
import com.guilherme.marvelcharacters.domain.repository.CharacterRepository
import com.guilherme.marvelcharacters.domain.usecase.InsertFavoriteCharacterUseCase
import io.mockk.MockKAnnotations
import io.mockk.coVerify
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class InsertFavoriteCharacterUseCaseTest {

    @RelaxedMockK
    private lateinit var characterRepository: CharacterRepository

    @InjectMockKs
    private lateinit var insertFavoriteCharacterUseCase: InsertFavoriteCharacterUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `invoke - check repository was called`() = runBlockingTest {
        val character = Character(
            id = 0,
            name = "Spider-Man",
            description = "",
            thumbnail = Image(path = "", extension = "")
        )

        insertFavoriteCharacterUseCase(character)

        coVerify { characterRepository.insertFavoriteCharacter(character) }
    }
}