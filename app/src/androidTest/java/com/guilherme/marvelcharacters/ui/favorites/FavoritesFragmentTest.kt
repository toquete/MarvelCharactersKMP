package com.guilherme.marvelcharacters.ui.favorites

import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.intent.Intents
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.guilherme.marvelcharacters.MainActivity
import com.guilherme.marvelcharacters.data.source.local.model.CharacterEntity
import com.guilherme.marvelcharacters.data.source.local.model.ImageEntity
import com.guilherme.marvelcharacters.infrastructure.database.CharacterDatabase
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class FavoritesFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    val rule = ActivityScenarioRule(MainActivity::class.java)

    @Inject
    lateinit var db: CharacterDatabase

    @Before
    fun setUp() {
        hiltRule.inject()
        Intents.init()
    }

    @After
    fun tearDown() {
        db.close()
        Intents.release()
    }

    @Test
    fun checkScreenIsDisplayed() {
        favorites {
            clickBottomItem()
            checkToolbarTitle()
            checkOverflowMenuIsNotDisplayed()
            checkBottomBarItemIsSelected()
        }
    }

    @Test
    fun checkCharacterDeletion() {
        mockFavoriteCharacter()

        favorites {
            clickBottomItem()
            clickOverflowMenu(ApplicationProvider.getApplicationContext())
            clickDeleteAllItem()
            clickDialogDelete()
            checkCharacterWasDeleted()
        }
    }

    @Test
    fun checkCharacterDeletionConfirmationDialog() {
        mockFavoriteCharacter()

        favorites {
            clickBottomItem()
            clickOverflowMenu(ApplicationProvider.getApplicationContext())
            clickDeleteAllItem()
            checkConfirmationDialog()
        }
    }

    @Test
    fun checkDetailScreenIsDisplayed() {
        mockFavoriteCharacter()

        favorites {
            clickBottomItem()
            clickItem("Spider-Man")
            checkDetailScreenIsDisplayed()
        }
    }

    private fun mockFavoriteCharacter() {
        val character = CharacterEntity(
            id = 1,
            name = "Spider-Man",
            description = "xablau",
            thumbnail = ImageEntity("", "")
        )
        GlobalScope.launch { db.characterDao().insert(character) }
    }
}