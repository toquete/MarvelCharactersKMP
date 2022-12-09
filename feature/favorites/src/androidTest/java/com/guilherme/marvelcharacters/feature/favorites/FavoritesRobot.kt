package com.guilherme.marvelcharacters.feature.favorites

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.scrollTo
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withChild
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText

fun favorites(func: FavoritesRobot.() -> Unit) = FavoritesRobot().apply { func() }

class FavoritesRobot {

    fun checkItemIsVisible(text: String) {
        onView(withId(R.id.recyclerViewFavorites))
            .perform(scrollTo<RecyclerView.ViewHolder>(withChild(withText(text))))

        onView(withText(text))
            .check(matches(isDisplayed()))
    }

    fun checkItemIsNotVisible(text: String) {
        onView(withText(text))
            .check(doesNotExist())
    }
}