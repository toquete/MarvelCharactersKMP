package com.guilherme.marvelcharacters.ui.favorites

import androidx.test.core.app.ApplicationProvider
import androidx.test.core.app.launchActivity
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.guilherme.marvelcharacters.MainActivity
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
    fun checkScreenIsDisplayed() {
        launchActivity<MainActivity>().use {
            favorites {
                clickBottomItem()
                checkToolbarTitle()
                checkOverflowMenuIsNotDisplayed()
                checkBottomBarItemIsSelected()
            }
        }
    }

    @Test
    fun checkCharacterDeletion() {
        mockFavoriteCharacter()

        launchActivity<MainActivity>().use {
            favorites {
                clickBottomItem()
                clickOverflowMenu(ApplicationProvider.getApplicationContext())
                clickDeleteAllItem()
                clickDialogDelete()
                checkCharacterWasDeleted()
            }
        }
    }

    @Test
    fun checkCharacterDeletionConfirmationDialog() {
        mockFavoriteCharacter()

        launchActivity<MainActivity>().use {
            favorites {
                clickBottomItem()
                clickOverflowMenu(ApplicationProvider.getApplicationContext())
                clickDeleteAllItem()
                checkConfirmationDialog()
            }
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