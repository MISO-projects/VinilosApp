package com.miso.vinilosapp

import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withClassName
import androidx.test.espresso.matcher.ViewMatchers.withContentDescription
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.miso.vinilosapp.ui.MainActivity
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.`is`
import org.hamcrest.TypeSafeMatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class AddAlbumTest {

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    private var decorView: View? = null

    @Before
    fun setUp() {
        mActivityScenarioRule.scenario.onActivity { activity ->
            decorView = activity.window.decorView
        }
    }

    @Test
    fun addAlbumTest() {
        val materialTextView = onView(
            allOf(
                withId(R.id.txtAlbumSection),
                withText("Albums"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.homeRv),
                        2
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        materialTextView.perform(click())

        val floatingActionButton = onView(
            allOf(
                withId(R.id.fab_add_album),
                withContentDescription("Crear album"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nav_host_fragment),
                        0
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        floatingActionButton.perform(click())

        val textInputEditText = onView(
            childAtPosition(
                childAtPosition(
                    withId(R.id.newAlbumName),
                    0
                ),
                0
            )
        )
        textInputEditText.perform(
            scrollTo(),
            replaceText("Dark side of the moon"),
            closeSoftKeyboard()
        )

        val textInputEditText2 = onView(
            childAtPosition(
                childAtPosition(
                    withId(R.id.newAlbumCover),
                    0
                ),
                0
            )
        )
        textInputEditText2.perform(scrollTo(), replaceText("http://image.com"), closeSoftKeyboard())

        val textInputEditText3 = onView(
            childAtPosition(
                childAtPosition(
                    withId(R.id.newAlbumReleaseDate),
                    0
                ),
                0
            )
        )
        textInputEditText3.perform(scrollTo(), click())

        onView(withText("OK"))
            .inRoot(RootMatchers.isDialog())
            .perform(click())

        val materialAutoCompleteTextView = onView(
            allOf(
                withId(R.id.newAlbumGenreAutoComplete),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.newAlbumGenre),
                        0
                    ),
                    0
                )
            )
        )
        materialAutoCompleteTextView.perform(scrollTo(), click())

        onView(withText("Rock"))
            .inRoot(RootMatchers.isPlatformPopup())
            .perform(click())

        val materialAutoCompleteTextView2 = onView(
            allOf(
                withId(R.id.newAlbumLabelAutoComplete),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.newAlbumLabel),
                        0
                    ),
                    0
                )
            )
        )
        materialAutoCompleteTextView2.perform(scrollTo(), click())

        onView(withText("EMI"))
            .inRoot(RootMatchers.isPlatformPopup())
            .perform(click())

        val textInputEditText4 = onView(
            childAtPosition(
                childAtPosition(
                    withId(R.id.newAlbumDescription),
                    0
                ),
                0
            )
        )
        textInputEditText4.perform(scrollTo(), replaceText("Descripcion"), closeSoftKeyboard())

        val materialButton2 = onView(
            allOf(
                withId(R.id.addAlbumBtn),
                withText("Crear album"),
                childAtPosition(
                    allOf(
                        withId(R.id.addAlbum),
                        childAtPosition(
                            withClassName(`is`("android.widget.ScrollView")),
                            0
                        )
                    ),
                    8
                )
            )
        )
        materialButton2.perform(scrollTo(), click())
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>,
        position: Int
    ): Matcher<View> {
        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent) &&
                    view == parent.getChildAt(position)
            }
        }
    }
}
