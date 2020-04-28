package com.guilherme.marvelcharacters.ui.home

import androidx.test.espresso.intent.rule.IntentsTestRule
import com.guilherme.marvelcharacters.BaseTest
import com.guilherme.marvelcharacters.MainActivity
import com.guilherme.marvelcharacters.data.model.Character
import com.guilherme.marvelcharacters.data.model.Container
import com.guilherme.marvelcharacters.data.model.Image
import com.guilherme.marvelcharacters.data.model.Result
import io.mockk.coEvery
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class HomeFragmentTest : BaseTest() {

    private val character = Character(id = 1, name = "Spider-Man", description = "xablau", thumbnail = Image("", ""))

    @get:Rule
    val rule = IntentsTestRule(MainActivity::class.java, true, true)

    @Before
    fun setUp() {
        val result = Result(Container(listOf(character)))
        coEvery { api.getCharacters(any(), any(), any(), any()) } returns result
    }

    @Test
    fun checkScreenIsDisplayed() {
        home {
            checkToolbarTitle()
            checkEditTextIsDisplayed()
            checkButtonIsDisplayed()
            checkBottomBarItemIsSelected()
        }
    }

    @Test
    fun searchCharacter() {
        home {
            clickEditText()
            typeEditText("spider")
            clickSearchButton()
            checkItemIsVisible("Spider-Man")
        }
    }

    @Test
    fun checkDetailScreenIsDisplayed() {
        home {
            clickEditText()
            typeEditText("spider")
            clickSearchButton()
            checkItemIsVisible("Spider-Man")
            clickItem("Spider-Man")
            checkDetailScreenIsDisplayed()
        }
    }
}