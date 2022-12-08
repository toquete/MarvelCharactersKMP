package com.guilherme.marvelcharacters.feature.detail

import androidx.core.os.bundleOf
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.guilherme.marvelcharacters.core.model.Character
import com.guilherme.marvelcharacters.domain.model.FavoriteCharacter
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
class DetailFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @BindValue
    internal val viewModel: DetailViewModel = mockk(relaxed = true)

    private val fakeState = MutableStateFlow<DetailUiState>(DetailUiState.Loading)

    private val bundle = bundleOf("characterId" to 0)

    @Before
    fun setUp() {
        every { viewModel.uiState } returns fakeState
    }

    @Test
    fun checkScreenIsDisplayed() {
        mockCharacter()

        launchFragmentInHiltContainer<DetailFragment>(
            fragmentArgs = bundle,
            themeResId = R.style.Theme_AppTheme
        )

        detail {
            checkToolbarTitle("Spider-Man")
            checkFabIsNotActivated()
            checkDescription("xablau")
        }
    }

    @Test
    fun checkScreenIsDisplayed_favoriteCharacter() {
        mockCharacter(isFavorite = true)

        launchFragmentInHiltContainer<DetailFragment>(
            fragmentArgs = bundle,
            themeResId = R.style.Theme_AppTheme
        )

        detail {
            checkToolbarTitle("Spider-Man")
            checkFabIsActivated()
            checkDescription("xablau")
        }
    }

    @Test
    fun checkCharacterIsDeleted() {
        mockCharacter(isFavorite = true)

        launchFragmentInHiltContainer<DetailFragment>(
            fragmentArgs = bundle,
            themeResId = R.style.Theme_AppTheme
        )

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

        launchFragmentInHiltContainer<DetailFragment>(
            fragmentArgs = bundle,
            themeResId = R.style.Theme_AppTheme
        )

        detail {
            checkFabIsNotActivated()
            clickFabButton()
            checkCharacterWasAdded()
            checkFabIsActivated()
        }
    }

    private fun mockCharacter(isFavorite: Boolean = false) {
        val character = Character(
            id = 1,
            name = "Spider-Man",
            description = "xablau",
            thumbnail = "test.jpg"
        )

        val favoriteCharacter = FavoriteCharacter(character, isFavorite)

        fakeState.value = DetailUiState.Success(favoriteCharacter)

        every { viewModel.onFabClick(isFavorite) } answers {
            fakeState.value =
                DetailUiState.Success(favoriteCharacter.copy(isFavorite = !isFavorite))
            val message = if (isFavorite) R.string.character_deleted else R.string.character_added
            fakeState.value = DetailUiState.ShowSnackbar(message, isFavorite)
        }
    }
}