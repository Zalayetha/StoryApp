package com.zaghy.storyapp.auth.login.view

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.zaghy.storyapp.R
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class LoginTest{


    @Test
    fun testLoginSuccess() {
        launchFragmentInContainer<Login>()
        // Input the email
        onView(withId(R.id.ed_login_email)).perform(typeText("123@test1.com"), closeSoftKeyboard())

        // Input the password
        onView(withId(R.id.ed_login_password)).perform(typeText("kagehina"), closeSoftKeyboard())

        // Click on the login button
        onView(withId(R.id.loginButton)).perform(click())

        // Check if the progress bar is displayed
        onView(withId(R.id.progressBar)).check(matches(isDisplayed()))

    }

    @Test
    fun testLoginFailure() {
        // Input the email
        onView(withId(R.id.ed_login_email)).perform(typeText("wrong@example.com"), closeSoftKeyboard())

        // Input the password
        onView(withId(R.id.ed_login_password)).perform(typeText("wrongPassword"), closeSoftKeyboard())

        // Click on the login button
        onView(withId(R.id.loginButton)).perform(click())

        // Check if the error dialog is displayed
        onView(withText("Information")).check(matches(isDisplayed()))
    }
}