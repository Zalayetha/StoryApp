package com.zaghy.storyapp.auth.login.view

import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragment
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.zaghy.storyapp.JsonConverter
import com.zaghy.storyapp.R
import com.zaghy.storyapp.network.retrofit.ApiConfig
import com.zaghy.storyapp.utils.EspressoIdlingResources
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class LoginTest{

    private val mockWebServer = MockWebServer()
    @Before
    fun setup(){
        mockWebServer.start(8080)
        ApiConfig.BASE_URL = "http://127.0.0.1:8080/"
        IdlingRegistry.getInstance().register(EspressoIdlingResources.countingIdlingResource)
    }

    @After
    fun teardown(){
        mockWebServer.shutdown()
        IdlingRegistry.getInstance().unregister(EspressoIdlingResources.countingIdlingResource)
    }
    @Test
    fun testLoginSuccess() {
        val bundle = Bundle()
        launchFragmentInContainer<Login>(bundle,R.style.Base_Theme_StoryApp)
        // Input the email
        onView(withId(R.id.ed_login_email)).perform(typeText("123@test1.com"), closeSoftKeyboard())

        // Input the password
        onView(withId(R.id.ed_login_password)).perform(typeText("kagehina"), closeSoftKeyboard())
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(JsonConverter.readStringFromFile("success_response_login.json"))
        mockWebServer.enqueue(mockResponse)
        // Click on the login button
        onView(withId(R.id.loginButton)).perform(click())

        onView(withId(R.id.progressBar)).check(matches(isDisplayed()))

    }

    @Test
    fun testLoginFailure() {
        val bundle = Bundle()
        launchFragmentInContainer<Login>(bundle,R.style.Base_Theme_StoryApp)
        // Input the email
        onView(withId(R.id.ed_login_email)).perform(typeText("wrong@example.com"), closeSoftKeyboard())

        // Input the password
        onView(withId(R.id.ed_login_password)).perform(typeText("wrongPassword"), closeSoftKeyboard())

        // Click on the login button
        onView(withId(R.id.loginButton)).perform(click())

        // Check if the error dialog is displayed
        onView(withText("Information")).check(matches(isDisplayed())).inRoot(isDialog())
    }
}