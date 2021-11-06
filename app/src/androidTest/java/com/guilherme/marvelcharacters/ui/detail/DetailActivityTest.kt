package com.guilherme.marvelcharacters.ui.detail

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import com.guilherme.marvelcharacters.BaseTest
import com.guilherme.marvelcharacters.data.source.local.model.CharacterEntity
import com.guilherme.marvelcharacters.data.source.local.model.ImageEntity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.junit.Test

class DetailActivityTest : BaseTest() {

    private lateinit var scenario: ActivityScenario<DetailActivity>

    override fun tearDown() {
        super.tearDown()
        scenario.close()
    }

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
        val character = CharacterEntity(
            id = 1,
            name = "Spider-Man",
            description = "xablau",
            thumbnail = ImageEntity("", "")
        )

        if (isFavorite) {
            GlobalScope.launch { db.characterDao().insert(character) }
        }

        Intent(ApplicationProvider.getApplicationContext(), DetailActivity::class.java).apply {
            putExtra("character", character)
            scenario = ActivityScenario.launch(this)
        }
    }
}