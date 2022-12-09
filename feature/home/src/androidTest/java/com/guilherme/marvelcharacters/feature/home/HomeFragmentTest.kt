package com.guilherme.marvelcharacters.feature.home

import androidx.test.ext.junit.runners.AndroidJUnit4
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
    internal val viewModel: HomeViewModel = mockk(relaxed = true)

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
        launchFragmentInHiltContainer<HomeFragment>(themeResId = R.style.Theme_AppTheme)

        home {
            checkEditTextIsDisplayed()
            checkButtonIsDisplayed()
        }
    }

    @Test
    fun searchCharacter() {
        mockApiSuccess()

        launchFragmentInHiltContainer<HomeFragment>(themeResId = R.style.Theme_AppTheme)

        home {
            clickEditText()
            typeEditText("spider")
            clickSearchButton()
            checkItemIsVisible("Spider-Man")
        }
    }

    @Test
    fun checkRequestErrorIsDisplayed() {
        mockApiError()

        launchFragmentInHiltContainer<HomeFragment>(themeResId = R.style.Theme_AppTheme)

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

        launchFragmentInHiltContainer<HomeFragment>(themeResId = R.style.Theme_AppTheme)

        home {
            clickEditText()
            typeEditText("spider")
            clickSearchButton()
            checkMessage("No characters with that name. Try again!")
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