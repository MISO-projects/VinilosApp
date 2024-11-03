package com.miso.vinilosapp

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.action.ViewActions.click;
import androidx.test.espresso.contrib.RecyclerViewActions

import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.miso.vinilosapp.ui.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.espresso.matcher.ViewMatchers.withId

@RunWith(AndroidJUnit4::class)
class AlbumDetailTest {

    @get:Rule
    val activityRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun testAlbumDetailThenViewTracks() {
        onView(withId(R.id.recyclerViewAlbumSection))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    0,
                    click()
                )
            )

        onView(withId(R.id.txtCancionesSection))
            .check(matches(isDisplayed()))

        onView(withId(R.id.songItemRv))
            .check(matches(isDisplayed()))

        onView(withId(R.id.backBtn)).perform(click())

        onView(withId(R.id.recyclerViewAlbumSection))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    1,
                    click()
                )
            )

        onView(withId(R.id.songItemRv))
            .check(matches(isDisplayed()))
    }



}