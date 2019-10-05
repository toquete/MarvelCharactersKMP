package com.guilherme.marvelcharacters.ui.main

import androidx.test.espresso.intent.rule.IntentsTestRule
import com.guilherme.marvelcharacters.BaseTest
import com.guilherme.marvelcharacters.data.model.Character
import com.guilherme.marvelcharacters.data.model.Container
import com.guilherme.marvelcharacters.data.model.Result
import io.mockk.coEvery
import org.junit.Rule
import org.junit.Test

class MainActivityTest : BaseTest() {

    @get:Rule
    val rule = IntentsTestRule(MainActivity::class.java, true, true)

    @Test
    fun searchCharacter() {
        coEvery { api.getCharacters(any(), any(), any(), any()) } returns Result(
            container = Container(
                characters = listOf(Character(id = 1, name = "Spider-Man", description = "xablau"))
            )
        )

        main {
            clickEditText()
            typeEditText("spider")
            clickSearchButton()
            checkItemIsVisible("Spider-Man")
        }
    }
}