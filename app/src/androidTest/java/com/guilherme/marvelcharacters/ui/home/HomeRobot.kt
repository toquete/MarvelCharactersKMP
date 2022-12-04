package com.guilherme.marvelcharacters.ui.home

import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.scrollTo
import androidx.test.espresso.matcher.ViewMatchers.*
import com.guilherme.marvelcharacters.R
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.core.AllOf.allOf

fun home(func: HomeRobot.() -> Unit) = HomeRobot().apply { func() }

class HomeRobot {

    fun clickEditText() {
        onView(withId(R.id.searchEditText))
            .perform(click())
    }

    fun typeEditText(text: String) {
        onView(withId(R.id.searchEditText))
            .perform(typeText(text))
    }

    fun clickSearchButton() {
        onView(withId(R.id.button))
            .perform(click())
    }

    fun checkItemIsVisible(text: String) {
        onView(withId(R.id.recyclerviewCharacters))
            .perform(scrollTo<RecyclerView.ViewHolder>(withChild(withText(text))))

        onView(withText(text))
            .check(matches(isDisplayed()))
    }

    fun checkToolbarTitle() {
        onView(allOf(instanceOf(TextView::class.java), withParent(withId(R.id.mainToolbar))))
            .check(matches(withText("Marvel Characters")))
    }

    fun checkEditTextIsDisplayed() {
        onView(withId(R.id.searchEditText))
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

    fun checkMessage(message: String) {
        onView(withId(R.id.textviewMessage))
            .check(matches(withText(message)))
    }
}