package com.miso.vinilosapp

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.miso.vinilosapp.ui.MainActivity
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.not
import org.hamcrest.core.AllOf.allOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class CreateTrackTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    private var decorView: View? = null

    @Before
    fun setUp() {
        activityRule.scenario.onActivity { activity ->
            decorView = activity.window.decorView
        }
    }

    @Test
    fun testCreateTrackFlow() {
        onView(withId(R.id.txtAlbumSection))
            .perform(click())

        onView(withId(R.id.albumsRv))
            .check(matches(isDisplayed()))

        onView(withId(R.id.albumsRv))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    0,
                    click()
                )
            )

        onView(withId(R.id.txtCancionesSection))
            .check(matches(isDisplayed()))

        onView(withId(R.id.txtCancionesSection))
            .perform(click())

        onView(withId(R.id.createTrackButton))
            .check(matches(isDisplayed()))

        onView(withId(R.id.createTrackButton))
            .perform(click())

        onView(withId(R.id.trackNameEditText))
            .perform(typeText("Desapariciones"))

        onView(withId(R.id.trackDurationEditText))
            .perform(typeText("6:29"))

        onView(withId(R.id.albumSpinner))
            .perform(click())
        onData(allOf(`is`(instanceOf(String::class.java)), `is`("Poeta del pueblo")))
            .perform(click())

        onView(withId(R.id.createTrackButton))
            .perform(click())

        onView(withId(R.id.txtCancionesSection))
            .check(matches(isDisplayed()))
    }

    @Test
    fun testCreateTrackBackFlow() {
        onView(withId(R.id.txtAlbumSection))
            .perform(click())

        onView(withId(R.id.albumsRv))
            .check(matches(isDisplayed()))

        onView(withId(R.id.albumsRv))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    0,
                    click()
                )
            )

        onView(withId(R.id.txtCancionesSection))
            .check(matches(isDisplayed()))

        onView(withId(R.id.txtCancionesSection))
            .perform(click())

        onView(withId(R.id.createTrackButton))
            .check(matches(isDisplayed()))

        onView(withId(R.id.backBtnTrack))
            .perform(click())
    }
}
