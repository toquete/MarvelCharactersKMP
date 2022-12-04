package com.guilherme.marvelcharacters.ui.home

import androidx.test.core.app.launchActivity
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.guilherme.marvelcharacters.MainActivity
import com.guilherme.marvelcharacters.R
import com.guilherme.marvelcharacters.core.model.Character
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class HomeFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @BindValue
    val viewModel: HomeViewModel = mockk(relaxed = true)

    private val fakeState = MutableStateFlow<HomeUiState>(HomeUiState.Empty)

    private val character = Character(
        id = 1,
        name = "Spider-Man",
        description = "xablau",
        thumbnail = "test.jpg"
    )

    @Before
    fun setUp() {
        every { viewModel.uiState } returns fakeState
    }

    @Test
    fun checkScreenIsDisplayed() {
        launchActivity<MainActivity>().use {
            home {
                checkToolbarTitle()
                checkEditTextIsDisplayed()
                checkButtonIsDisplayed()
                checkBottomBarItemIsSelected()
            }
        }
    }

    @Test
    fun searchCharacter() {
        mockApiSuccess()

        launchActivity<MainActivity>().use {
            home {
                clickEditText()
                typeEditText("spider")
                clickSearchButton()
                checkItemIsVisible("Spider-Man")
            }
        }
    }

    @Test
    fun checkRequestErrorIsDisplayed() {
        mockApiError()

        launchActivity<MainActivity>().use {
            home {
                clickEditText()
                typeEditText("spider")
                clickSearchButton()
                checkMessage("There was an error with your request. Try again later!")
            }
        }
    }

    @Test
    fun checkEmptyStateMessageIsDisplayed() {
        mockApiSuccess(isEmpty = true)

        launchActivity<MainActivity>().use {
            home {
                clickEditText()
                typeEditText("spider")
                clickSearchButton()
                checkMessage("No characters with that name. Try again!")
            }
        }
    }

    private fun mockApiSuccess(isEmpty: Boolean = false) {
        every { viewModel.onSearchCharacter(any()) } answers {
            if (isEmpty) {
                fakeState.value = HomeUiState.Error(R.string.empty_state_message)
            } else {
                fakeState.value = HomeUiState.Success(listOf(character))
            }
        }
    }

    private fun mockApiError() {
        every { viewModel.onSearchCharacter(any()) } answers {
            fakeState.value = HomeUiState.Error(R.string.request_error_message)
        }
    }
}