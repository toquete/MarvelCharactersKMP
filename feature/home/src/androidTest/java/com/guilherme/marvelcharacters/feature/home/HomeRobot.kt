package com.guilherme.marvelcharacters.feature.home

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.scrollTo
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withChild
import androidx.test.espresso.matcher.ViewMatchers.withHint
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
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

    fun checkEditTextIsDisplayed() {
        onView(withId(R.id.searchEditText))
            .check(matches(allOf(isDisplayed(), withHint("Character"))))
    }

    fun checkButtonIsDisplayed() {
        onView(withId(R.id.button))
            .check(matches(allOf(isDisplayed(), withText("search"))))
    }

    fun checkMessage(message: String) {
        onView(withId(R.id.textviewMessage))
            .check(matches(withText(message)))
    }
}