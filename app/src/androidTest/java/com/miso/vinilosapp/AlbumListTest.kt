package com.miso.vinilosapp

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.miso.vinilosapp.ui.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.hamcrest.core.AllOf.allOf

@RunWith(AndroidJUnit4::class)
class AlbumListTest {

    @get:Rule
    val activityRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun testHomeToAlbumListAndThenDetail() {
        onView(withId(R.id.txtAlbumSection))
            .perform(click())

        onView(withId(R.id.albumsRv))
            .check(matches(isDisplayed()))

        onView(withId(R.id.collapsing_toolbar))
            .check(matches(isDisplayed()))

        onView(withId(R.id.title))
            .check(matches(withText(R.string.collapsing_toolbar_album_title)))

        onView(withId(R.id.subtitle))
            .check(matches(withText(R.string.collapsing_toolbar_album_subtitle)))

        onView(withId(R.id.albumsRv))
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(0))
            .check(matches(isDisplayed()))

        allOf(withId(R.id.imageView_album), isDisplayed())
        allOf(withId(R.id.textView_album_name), isDisplayed())
        allOf(withId(R.id.textView_album_description), isDisplayed())
        allOf(withId(R.id.textView_album_label), isDisplayed())

        onView(withId(R.id.albumsRv))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    0,
                    click()
                )
            )

        onView(withId(R.id.tv_title_detail))
            .check(matches(isDisplayed()))

        onView(withId(R.id.tv_title_detail))
            .check(matches(withText("Fragmento del detalle del Album")))
    }

}