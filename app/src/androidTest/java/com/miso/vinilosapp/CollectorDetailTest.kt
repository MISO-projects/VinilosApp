package com.miso.vinilosapp

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
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
class CollectorDetailTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun testCollectorDetailFlow() {
        onView(withId(R.id.txtCollectorSection))
            .perform(click())

        onView(withId(R.id.collectorsRv))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    0,
                    click()
                )
            )

        onView(withId(R.id.collectorDetail))
            .check(matches(isDisplayed()))

        allOf(withId(R.id.backBtnCollector), isDisplayed())
        allOf(withId(R.id.collectorName), isDisplayed())
        allOf(withId(R.id.collectorInfo), isDisplayed())
    }

    @Test
    fun testCollectorDetailThenViewAlbums() {
        onView(withId(R.id.txtCollectorSection))
            .perform(click())

        onView(withId(R.id.collectorsRv))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    0,
                    click()
                )
            )

        onView(withId(R.id.collector_albums_title))
            .check(matches(isDisplayed()))

        onView(withId(R.id.albumsCollectorRv))
            .check(matches(isDisplayed()))

        pressBack()

        onView(withId(R.id.collectorsRv))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    1,
                    click()
                )
            )

        onView(withId(R.id.albumsCollectorRv))
            .check(matches(isDisplayed()))
    }
}
