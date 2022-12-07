package com.guilherme.marvelcharacters.feature.detail

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.guilherme.marvelcharacters.core.testing.matcher.CustomMatchers.isActivated
import com.guilherme.marvelcharacters.core.testing.matcher.CustomMatchers.withTitle
import org.hamcrest.CoreMatchers.not

fun detail(func: DetailRobot.() -> Unit) = DetailRobot().apply { func() }

class DetailRobot {

    fun checkToolbarTitle(title: String) {
        onView(withId(R.id.collapsingToolbarLayout))
            .check(matches(withTitle(title)))
    }

    fun checkFabIsActivated() {
        onView(withId(R.id.fab))
            .check(matches(isActivated()))
    }

    fun checkFabIsNotActivated() {
        onView(withId(R.id.fab))
            .check(matches(not(isActivated())))
    }

    fun checkDescription(text: String) {
        onView(withId(R.id.description))
            .check(matches(withText(text)))
    }

    fun clickFabButton() {
        onView(withId(R.id.fab))
            .perform(click())
    }

    fun checkCharacterWasDeleted() {
        onView(withText("Character deleted!"))
            .check(matches(isDisplayed()))
    }

    fun checkCharacterWasAdded() {
        onView(withText("Character added!"))
            .check(matches(isDisplayed()))
    }
}