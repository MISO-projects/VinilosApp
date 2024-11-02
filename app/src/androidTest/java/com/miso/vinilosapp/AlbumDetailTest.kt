package com.miso.vinilosapp

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.miso.vinilosapp.ui.MainActivity
import org.hamcrest.core.AllOf.allOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@LargeTest
@RunWith(AndroidJUnit4::class)
class AlbumDetailTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun testAlbumDetailFlow() {
        onView(withId(R.id.txtAlbumSection))
            .perform(click())

        onView(withId(R.id.albumsRv))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    0,
                    click()
                )
            )

        onView(withId(R.id.albumDetail))
            .check(matches(isDisplayed()))

        allOf(withId(R.id.backBtn), isDisplayed())
        allOf(withId(R.id.albumImage), isDisplayed())
        allOf(withId(R.id.albumTitle), isDisplayed())
        allOf(withId(R.id.albumGenre), isDisplayed())
        allOf(withId(R.id.albumDescription), isDisplayed())
    }
}