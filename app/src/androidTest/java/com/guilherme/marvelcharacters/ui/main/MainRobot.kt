package com.guilherme.marvelcharacters.ui.main

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.scrollTo
import androidx.test.espresso.matcher.ViewMatchers.*
import com.guilherme.marvelcharacters.R

fun main(func: MainRobot.() -> Unit) = MainRobot().apply { func() }

class MainRobot {

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
}