package com.example.diaryapp.auth

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.example.diaryapp.presentation.screens.main.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test

@ExperimentalAnimationApi
@ExperimentalMaterial3Api
@HiltAndroidTest
class AuthenticationScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Test
    fun displayWelcomeMessage() {
        launchAuthenticationScreen(composeTestRule) {
            // do nothing
        } verify {
            welcomeMessageIsVisible()
        }
    }
}