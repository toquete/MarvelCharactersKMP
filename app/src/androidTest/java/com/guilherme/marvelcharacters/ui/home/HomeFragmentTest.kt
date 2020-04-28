package com.guilherme.marvelcharacters.ui.home

import androidx.test.espresso.intent.rule.IntentsTestRule
import com.guilherme.marvelcharacters.BaseTest
import com.guilherme.marvelcharacters.MainActivity
import com.guilherme.marvelcharacters.data.model.Character
import com.guilherme.marvelcharacters.data.model.Container
import com.guilherme.marvelcharacters.data.model.Image
import com.guilherme.marvelcharacters.data.model.Result
import io.mockk.coEvery
import org.junit.Rule
import org.junit.Test

class HomeFragmentTest : BaseTest() {

    private val character = Character(id = 1, name = "Spider-Man", description = "xablau", thumbnail = Image("", ""))

    @get:Rule
    val rule = IntentsTestRule(MainActivity::class.java, true, true)

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
        mockApiSuccess()

        home {
            clickEditText()
            typeEditText("spider")
            clickSearchButton()
            checkItemIsVisible("Spider-Man")
        }
    }

    @Test
    fun checkDetailScreenIsDisplayed() {
        mockApiSuccess()

        home {
            clickEditText()
            typeEditText("spider")
            clickSearchButton()
            checkItemIsVisible("Spider-Man")
            clickItem("Spider-Man")
            checkDetailScreenIsDisplayed()
        }
    }

    @Test
    fun checkErrorIsDisplayed() {
        mockApiError("This is an error")

        home {
            clickEditText()
            typeEditText("spider")
            clickSearchButton()
            checkMessage("This is an error")
        }
    }

    @Test
    fun checkEmptyStateMessageIsDisplayed() {
        mockApiSuccess(isEmpty = true)

        home {
            clickEditText()
            typeEditText("spider")
            clickSearchButton()
            checkMessage("No characters with that name. Try again!")
        }
    }

    private fun mockApiSuccess(isEmpty: Boolean = false) {
        val result = Result(Container(if (isEmpty) emptyList() else listOf(character)))
        coEvery { api.getCharacters(any(), any(), any(), any()) } returns result
    }

    private fun mockApiError(message: String) {
        coEvery { api.getCharacters(any(), any(), any(), any()) } throws IllegalArgumentException(message)
    }
}