package com.guilherme.marvelcharacters.ui.detail

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.guilherme.marvelcharacters.cache.dao.CharacterDatabase
import com.guilherme.marvelcharacters.cache.model.CharacterEntity
import com.guilherme.marvelcharacters.cache.model.ImageEntity
import com.guilherme.marvelcharacters.ui.model.CharacterVO
import com.guilherme.marvelcharacters.ui.model.ImageVO
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
class DetailActivityTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var db: CharacterDatabase

    private lateinit var scenario: ActivityScenario<DetailActivity>

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @After
    fun tearDown() {
        db.close()
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
        val characterVO = CharacterVO(
            id = 1,
            name = "Spider-Man",
            description = "xablau",
            thumbnail = ImageVO("", "")
        )
        val character = CharacterEntity(
            characterVO.id,
            characterVO.name,
            characterVO.description,
            ImageEntity(
                characterVO.thumbnail.path,
                characterVO.thumbnail.extension
            )
        )

        if (isFavorite) {
            GlobalScope.launch { db.characterDao().insert(character) }
        }

        Intent(ApplicationProvider.getApplicationContext(), DetailActivity::class.java).apply {
            putExtra("character", characterVO)
            scenario = ActivityScenario.launch(this)
        }
    }
}