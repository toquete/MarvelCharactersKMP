package com.guilherme.marvelcharacters.domain

import com.guilherme.marvelcharacters.core.model.Character
import com.guilherme.marvelcharacters.core.model.Image
import com.guilherme.marvelcharacters.data.repository.CharacterRepository
import com.guilherme.marvelcharacters.domain.usecase.DeleteFavoriteCharacterUseCase
import io.mockk.MockKAnnotations
import io.mockk.coVerify
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class DeleteFavoriteCharacterUseCaseTest {

    @RelaxedMockK
    private lateinit var characterRepository: CharacterRepository

    @InjectMockKs
    private lateinit var deleteFavoriteCharacterUseCase: DeleteFavoriteCharacterUseCase

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

        deleteFavoriteCharacterUseCase(character)

        coVerify { characterRepository.deleteFavoriteCharacter(character) }
    }
}