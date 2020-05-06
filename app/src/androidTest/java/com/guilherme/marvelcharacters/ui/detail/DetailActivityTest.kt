package com.guilherme.marvelcharacters.ui.detail

import android.content.Intent
import androidx.test.espresso.intent.rule.IntentsTestRule
import com.guilherme.marvelcharacters.BaseTest
import com.guilherme.marvelcharacters.data.model.Character
import com.guilherme.marvelcharacters.data.model.Image
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.junit.Rule
import org.junit.Test

class DetailActivityTest : BaseTest() {

    @get:Rule
    val rule = IntentsTestRule(DetailActivity::class.java, true, false)

    @Test
    fun checkScreenIsDisplayed() {
        mockCharacter()

        detail {
            checkToolbarTitle("Spider-Man")
            checkFabIsNotActivated()
            checkDescription("xablau")
        }
    }

    @Test
    fun checkScreenIsDisplayed_favoriteCharacter() {
        mockCharacter(isFavorite = true)

        detail {
            checkToolbarTitle("Spider-Man")
            checkFabIsActivated()
            checkDescription("xablau")
        }
    }

    @Test
    fun checkCharacterIsDeleted() {
        mockCharacter(isFavorite = true)

        detail {
            checkFabIsActivated()
            clickFabButton()
            checkCharacterWasDeleted()
            checkFabIsNotActivated()
        }
    }

    @Test
    fun checkCharacterIsAdded() {
        mockCharacter()

        detail {
            checkFabIsNotActivated()
            clickFabButton()
            checkCharacterWasAdded()
            checkFabIsActivated()
        }
    }

    private fun mockCharacter(isFavorite: Boolean = false) {
        val character = Character(id = 1, name = "Spider-Man", description = "xablau", thumbnail = Image("", ""))

        if (isFavorite) {
            GlobalScope.launch { db.characterDao().insert(character) }
        }

        Intent().apply {
            putExtra("character", character)
            rule.launchActivity(this)
        }
    }
}