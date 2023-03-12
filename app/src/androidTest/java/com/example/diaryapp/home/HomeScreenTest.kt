package com.example.diaryapp.home

import android.content.res.Resources
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.platform.app.InstrumentationRegistry
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test

@ExperimentalAnimationApi
@ExperimentalMaterial3Api
@HiltAndroidTest
class HomeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    private val resources: Resources = InstrumentationRegistry
        .getInstrumentation().targetContext.resources

    @Test
    fun displayTitle() {
        launchHomeScreen(composeTestRule, resources) {
            // do nothing
        } verify {
            titleIsVisible()
        }
    }
}