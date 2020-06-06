package com.android.basics.utils

import android.R
import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.DatePicker
import androidx.annotation.IdRes
import androidx.annotation.NonNull
import androidx.annotation.StringRes
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.PickerActions
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.RootMatchers.withDecorView
import androidx.test.espresso.matcher.ViewMatchers.*
import com.google.android.material.textfield.TextInputLayout
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.AllOf.allOf
import org.hamcrest.core.Is.`is`
import org.hamcrest.core.IsEqual.equalTo
import org.hamcrest.core.IsNot.not


open class ScreenRobot {
    private var activityContext // Only required for some calls
            : Activity? = null

    private fun hasTextInputLayoutHintText(expectedErrorText: String?): Matcher<View> =
        object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description?) {}
            override fun matchesSafely(item: View?): Boolean {
                if (item !is TextInputLayout) return false
                val error = item.hint ?: return false
                val hint = error.toString()
                return expectedErrorText == hint
            }
        }

    fun checkIsDisplayed(@IdRes vararg viewIds: Int) {
        for (viewId in viewIds) {
            onView(withId(viewId)).check(matches(isDisplayed()))
        }

    }

    fun checkIsHidden(@IdRes vararg viewIds: Int) {
        for (viewId in viewIds) {
            onView(withId(viewId)).check(matches(not(isDisplayed())))
        }

    }

    fun checkTextInputLayoutHasText(@IdRes viewId: Int, @StringRes messageResId: Int) {
        onView(withId(viewId)).check(matches(hasTextInputLayoutHintText(getString(messageResId))))
    }

    fun checkViewHasText(@IdRes viewId: Int, expected: String?) {
        onView(withId(viewId)).check(matches(withText(expected)))

    }

    fun checkViewHasText(@IdRes viewId: Int, @StringRes messageResId: Int) {
        onView(withId(viewId)).check(matches(withText(messageResId)))

    }

    fun checkViewHasHint(@IdRes viewId: Int, @StringRes messageResId: Int) {
        onView(withId(viewId)).check(matches(withHint(messageResId)))

    }

    fun clickOnView(@IdRes viewId: Int) {
        onView(withId(viewId)).perform(click())

    }

    fun replaceTextIntoView(@IdRes viewId: Int, text: String?) {
        onView(withId(viewId)).perform(replaceText(text))

    }

    fun enterTextIntoView(@IdRes viewId: Int, text: String?) {
        onView(withId(viewId)).perform(typeText(text))

    }

    fun clearTextFromView(@IdRes viewId: Int) {
        onView(withId(viewId)).perform(clearText())

    }

    fun closeKeyBoardForView(@IdRes viewId: Int) {
        onView(withId(viewId)).perform(closeSoftKeyboard())
    }

    fun enterTextIntoViewAndCloseKeyBoard(@IdRes viewId: Int, text: String?) {
        onView(withId(viewId)).perform(typeText(text))
            .perform(closeSoftKeyboard())
    }

    fun provideActivityContext(activityContext: Activity?) {
        this.activityContext = activityContext
    }

    fun checkDialogWithTextIsDisplayed(@StringRes messageResId: Int) {
        onView(withText(messageResId))
            .inRoot(withDecorView(not(activityContext!!.window.decorView)))
            .check(matches(isDisplayed()))
    }

    fun checkDialogWithButtonTextIsDisplayed(text: String?) {
        onView(withText(text))
            .inRoot(isDialog())
            .check(matches(isDisplayed()))

    }

    fun clickButtonTextOnDialog(text: String?) {
        onView(withText(text))
            .inRoot(isDialog())
            .check(matches(isDisplayed()))
            .perform(click())

    }

    fun swipeLeftOnView(@IdRes viewId: Int) {
        onView(withId(viewId)).perform(swipeLeft())

    }

    fun checkActionBarTitle(@StringRes messageResId: Int) {
        onView(
            allOf(
                isDescendantOfA(withResourceName("R.id.action_bar_container")),
                withText(messageResId)
            )
        )
            .check(matches(isDisplayed()))

    }

    fun checkToolbarTitle(title: CharSequence?) {
        onView(isAssignableFrom(Toolbar::class.java))
            .check(matches(withToolbarTitle(`is`(title))))

    }

    private fun withToolbarTitle(
        textMatcher: Matcher<CharSequence?>?
    ): Matcher<Any?>? {
        return object : BoundedMatcher<Any?, Toolbar?>(Toolbar::class.java) {
            public override fun matchesSafely(toolbar: Toolbar?): Boolean {
                return textMatcher?.matches(toolbar?.title)!!
            }

            override fun describeTo(description: Description?) {
                description?.appendText("with toolbar title: ")
                textMatcher?.describeTo(description)
            }
        }
    }

    fun getString(@StringRes messageResId: Int): String? {
        return ApplicationProvider.getApplicationContext<Context?>()?.resources
            ?.getString(messageResId)
    }

    fun enterDateIntoView(@IdRes viewId: Int, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        onView(withId(viewId)).perform(click())
        onView(withClassName(equalTo(DatePicker::class.java.name)))
            .perform(PickerActions.setDate(year, monthOfYear, dayOfMonth))
        onView(withText(R.string.ok)).perform(click())

    }

    fun checkRecyclerViewItem(@IdRes viewId: Int, position: Int, text: String?) {
        onView(withId(viewId))
            .check(matches(atPosition(position, hasDescendant(withText(text)))));
    }

    private fun atPosition(
        position: Int,
        @NonNull itemMatcher: Matcher<View?>
    ): Matcher<View?>? {
        return object : BoundedMatcher<View?, RecyclerView>(RecyclerView::class.java) {
            override fun describeTo(description: Description) {
                description.appendText("has item at position $position: ")
                itemMatcher.describeTo(description)
            }

            override fun matchesSafely(view: RecyclerView): Boolean {
                val viewHolder =
                    view.findViewHolderForAdapterPosition(position)
                        ?: // has no item on such position
                        return false
                return itemMatcher.matches(viewHolder.itemView)
            }
        }
    }


}