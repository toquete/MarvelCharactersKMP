package com.guilherme.marvelcharacters.util

import android.view.View
import androidx.test.espresso.matcher.BoundedMatcher
import com.google.android.material.appbar.CollapsingToolbarLayout
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher


object CustomMatchers {

    fun isActivated(): Matcher<View> = object : TypeSafeMatcher<View>() {
        override fun describeTo(description: Description?) {
            description?.appendText("is activated")
        }

        override fun matchesSafely(item: View?): Boolean = item?.isActivated ?: false
    }

    fun withTitle(text: String): Matcher<View> {
        return object : BoundedMatcher<View, CollapsingToolbarLayout>(CollapsingToolbarLayout::class.java) {
            override fun describeTo(description: Description?) {
                description?.appendText("CollapsingToolbarLayout with text: $text")
            }

            override fun matchesSafely(item: CollapsingToolbarLayout?): Boolean {
                return item?.title == text
            }

        }
    }
}