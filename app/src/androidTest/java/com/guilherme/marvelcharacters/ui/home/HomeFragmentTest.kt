package com.guilherme.marvelcharacters.ui.home

import androidx.test.espresso.intent.Intents
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.guilherme.marvelcharacters.BaseTest
import com.guilherme.marvelcharacters.MainActivity
import com.guilherme.marvelcharacters.data.model.CharacterResponse
import com.guilherme.marvelcharacters.data.model.ContainerResponse
import com.guilherme.marvelcharacters.data.model.ImageResponse
import com.guilherme.marvelcharacters.data.model.Response as apiResponse
import io.mockk.coEvery
import okhttp3.ResponseBody
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response

class HomeFragmentTest : BaseTest() {

    private val character = CharacterResponse(
        id = 1,
        name = "Spider-Man",
        description = "xablau",
        thumbnail = ImageResponse(
            path = "",
            extension = ""
        )
    )

    @get:Rule
    val rule = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun setUp() {
        Intents.init()
    }

    override fun tearDown() {
        super.tearDown()
        Intents.release()
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
            clickItem("Spider-Man")
            checkDetailScreenIsDisplayed()
        }
    }

    @Test
    fun checkRequestkErrorIsDisplayed() {
        val exception = HttpException(
            Response.error<String>(
                404,
                ResponseBody.create(null, "error")
            )
        )
        mockApiError(exception)

        home {
            clickEditText()
            typeEditText("spider")
            clickSearchButton()
            checkMessage("There was an error with your request. Try again later!")
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
        val result = apiResponse(ContainerResponse(if (isEmpty) emptyList() else listOf(character)))
        coEvery { api.getCharacters(any(), any(), any(), any()) } returns result
    }

    private fun mockApiError(exception: Exception) {
        coEvery { api.getCharacters(any(), any(), any(), any()) } throws exception
    }
}