package com.guilherme.marvelcharacters.ui.home

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItem
import androidx.test.espresso.contrib.RecyclerViewActions.scrollTo
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.*
import com.guilherme.marvelcharacters.R
import com.guilherme.marvelcharacters.ui.detail.DetailActivity

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

    fun clickItem(text: String) {
        onView(withId(R.id.recyclerviewCharacters))
            .perform(actionOnItem<RecyclerView.ViewHolder>(withText(text), click()))
    }

    fun checkDetailScreenIsDisplayed() {
        intended(hasComponent(DetailActivity::class.java.name))
    }
}