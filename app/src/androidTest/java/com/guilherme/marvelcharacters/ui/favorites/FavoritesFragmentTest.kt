package com.guilherme.marvelcharacters.ui.favorites

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.guilherme.marvelcharacters.R
import com.guilherme.marvelcharacters.core.model.Character
import com.guilherme.marvelcharacters.ui.launchFragmentInHiltContainer
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
class FavoritesFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @BindValue
    val viewModel: FavoritesViewModel = mockk(relaxed = true)

    private val fakeState = MutableStateFlow<FavoritesUiState>(FavoritesUiState.Success())

    @Before
    fun setUp() {
        every { viewModel.uiState } returns fakeState
    }

    @Test
    fun checkEmptyScreenIsDisplayed() {
        launchFragmentInHiltContainer<FavoritesFragment>(themeResId = R.style.Theme_AppTheme)

        favorites {
            checkItemIsNotVisible("Spider-Man")
        }
    }

    @Test
    fun checkListScreenIsDisplayed() {
        mockFavoriteCharacter()

        launchFragmentInHiltContainer<FavoritesFragment>(themeResId = R.style.Theme_AppTheme)

        favorites {
            checkItemIsVisible("Spider-Man")
        }
    }

    private fun mockFavoriteCharacter() {
        val character = Character(
            id = 1,
            name = "Spider-Man",
            description = "xablau",
            thumbnail = "test.jpg"
        )

        fakeState.value = FavoritesUiState.Success(listOf(character))
    }
}