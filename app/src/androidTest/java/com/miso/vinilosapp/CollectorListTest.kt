package com.miso.vinilosapp

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.miso.vinilosapp.ui.MainActivity
import org.hamcrest.core.AllOf.allOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CollectorListTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun testHomeToCollectorList() {
        onView(withId(R.id.txtCollectorSection))
            .perform(click())

        onView(withId(R.id.collectorsRv))
            .check(matches(isDisplayed()))

        onView(withId(R.id.collector_collapsing_toolbar))
            .check(matches(isDisplayed()))

        onView(withId(R.id.collector_collapsing_toolbar_title))
            .check(matches(withText(R.string.collapsing_toolbar_collector_title)))

        onView(withId(R.id.collector_collapsing_toolbar_subtitle))
            .check(matches(withText(R.string.collapsing_toolbar_collector_subtitle)))

        onView(withId(R.id.collectorsRv))
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(0))
            .check(matches(isDisplayed()))

        allOf(withId(R.id.textView_collector_initials), isDisplayed())
        allOf(withId(R.id.textView_collector_name), isDisplayed())
        allOf(withId(R.id.textView_collector_email), isDisplayed())
    }
}