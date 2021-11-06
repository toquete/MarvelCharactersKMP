package com.guilherme.marvelcharacters.ui.favorites

import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.intent.Intents
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.guilherme.marvelcharacters.BaseTest
import com.guilherme.marvelcharacters.MainActivity
import com.guilherme.marvelcharacters.data.source.local.model.CharacterEntity
import com.guilherme.marvelcharacters.data.source.local.model.ImageEntity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class FavoritesFragmentTest : BaseTest() {

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