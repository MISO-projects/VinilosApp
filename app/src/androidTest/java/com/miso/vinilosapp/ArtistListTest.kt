package com.miso.vinilosapp

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.miso.vinilosapp.ui.MainActivity
import org.hamcrest.core.AllOf.allOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ArtistListTest {

    @get:Rule
    val activityRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun testHomeToArtistListAndThenDetail() {
        onView(withId(R.id.txtArtistSection))
            .perform(click())

        onView(withId(R.id.artistsRv))
            .check(matches(isDisplayed()))

        onView(withId(R.id.collapsing_toolbar))
            .check(matches(isDisplayed()))

        onView(withId(R.id.title))
            .check(matches(withText(R.string.collapsing_toolbar_artist_title)))

        onView(withId(R.id.subtitle))
            .check(matches(withText(R.string.collapsing_toolbar_artist_subtitle)))

        onView(withId(R.id.artistsRv))
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(0))
            .check(matches(isDisplayed()))

        allOf(withId(R.id.imageView_artist), isDisplayed())
        allOf(withId(R.id.textView_artist_name), isDisplayed())
        allOf(withId(R.id.textView_artist_description), isDisplayed())
        allOf(withId(R.id.textView_artist_label), isDisplayed())

        onView(withId(R.id.artistsRv))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    0,
                    click()
                )
            )

        onView(withId(R.id.artistName))
            .check(matches(isDisplayed()))
    }
}
