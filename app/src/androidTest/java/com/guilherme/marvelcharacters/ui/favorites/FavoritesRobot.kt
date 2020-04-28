package com.guilherme.marvelcharacters.ui.favorites

import android.content.Context
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItem
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isSelected
import androidx.test.espresso.matcher.ViewMatchers.withClassName
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withParent
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.guilherme.marvelcharacters.R
import com.guilherme.marvelcharacters.ui.detail.DetailActivity
import org.hamcrest.CoreMatchers.endsWith
import org.hamcrest.core.AllOf.allOf
import org.hamcrest.core.IsInstanceOf.instanceOf

fun favorites(func: FavoritesRobot.() -> Unit) = FavoritesRobot().apply { func() }

class FavoritesRobot {

    fun clickBottomItem() {
        onView(withId(R.id.favoritesFragment))
            .perform(click())
    }

    fun checkToolbarTitle() {
        onView(allOf(instanceOf(TextView::class.java), withParent(withId(R.id.mainToolbar))))
            .check(matches(withText("Favorites")))
    }

    fun checkBottomBarItemIsSelected() {
        onView(withId(R.id.favoritesFragment))
            .check(matches(isSelected()))
    }

    fun checkOverflowMenuIsNotDisplayed() {
        onView(withClassName(endsWith("OverflowMenuButton")))
            .check(doesNotExist())
    }

    fun clickItem(text: String) {
        onView(withId(R.id.recyclerViewFavorites))
            .perform(actionOnItem<RecyclerView.ViewHolder>(withText(text), click()))
    }

    fun clickOverflowMenu(context: Context) {
        openActionBarOverflowOrOptionsMenu(context)
    }

    fun clickDeleteAllItem() {
        onView(withText("Delete all"))
            .perform(click())
    }

    fun clickDialogDelete() {
        onView(withText("delete"))
            .perform(click())
    }

    fun checkCharacterWasDeleted() {
        onView(withText("Character deleted!"))
            .check(matches(isDisplayed()))
    }

    fun checkDetailScreenIsDisplayed() {
        intended(hasComponent(DetailActivity::class.java.name))
    }

    fun checkConfirmationDialog() {
        onView(withId(R.id.alertTitle))
            .check(matches(withText("Delete all")))

        onView(withId(android.R.id.message))
            .check(matches(withText("Delete all favorite characters?")))

        onView(withId(android.R.id.button1))
            .check(matches(withText("delete")))

        onView(withId(android.R.id.button2))
            .check(matches(withText("Cancel")))
    }
}