package com.guilherme.marvelcharacters.ui.home

import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItem
import androidx.test.espresso.contrib.RecyclerViewActions.scrollTo
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isSelected
import androidx.test.espresso.matcher.ViewMatchers.withHint
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withParent
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.guilherme.marvelcharacters.R
import com.guilherme.marvelcharacters.ui.detail.DetailActivity
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.core.AllOf.allOf

fun home(func: HomeRobot.() -> Unit) = HomeRobot().apply { func() }

class HomeRobot {

    fun clickEditText() {
        onView(withId(R.id.editText))
            .perform(click())
    }

    fun typeEditText(text: String) {
        onView(withId(R.id.editText))
            .perform(typeText(text))
    }

    fun clickSearchButton() {
        onView(withId(R.id.button))
            .perform(click())
    }

    fun checkItemIsVisible(text: String) {
        onView(withId(R.id.recyclerviewCharacters))
            .perform(scrollTo<RecyclerView.ViewHolder>(withText(text)))

        onView(withText(text))
            .check(matches(isDisplayed()))
    }

    fun clickItem(text: String) {
        onView(withId(R.id.recyclerviewCharacters))
            .perform(actionOnItem<RecyclerView.ViewHolder>(withText(text), click()))
    }

    fun checkDetailScreenIsDisplayed() {
        intended(hasComponent(DetailActivity::class.java.name))
    }

    fun checkToolbarTitle() {
        onView(allOf(instanceOf(TextView::class.java), withParent(withId(R.id.mainToolbar))))
            .check(matches(withText("Marvel Characters")))
    }

    fun checkEditTextIsDisplayed() {
        onView(withId(R.id.editText))
            .check(matches(allOf(isDisplayed(), withHint("Character"))))
    }

    fun checkButtonIsDisplayed() {
        onView(withId(R.id.button))
            .check(matches(allOf(isDisplayed(), withText("search"))))
    }

    fun checkBottomBarItemIsSelected() {
        onView(withId(R.id.homeFragment))
            .check(matches(isSelected()))
    }
}