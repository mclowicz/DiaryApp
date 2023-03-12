package com.example.diaryapp.auth

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.example.diaryapp.presentation.screens.main.MainActivity
import com.example.diaryapp.R

@ExperimentalMaterial3Api
@ExperimentalAnimationApi
fun launchAuthenticationScreen(
    rule: AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>,
    block: AuthenticationRobot.() -> Unit
): AuthenticationRobot = AuthenticationRobot(rule).apply(block)

@ExperimentalMaterial3Api
@ExperimentalAnimationApi
class AuthenticationRobot(
    private val rule: AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>
) {

    infix fun verify(block: AuthenticationVerification.() -> Unit): AuthenticationVerification {
        return AuthenticationVerification(rule).apply(block)
    }

}

@ExperimentalMaterial3Api
@ExperimentalAnimationApi
class AuthenticationVerification(
    private val rule: AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>
) {
    fun welcomeMessageIsVisible() {
        val welcomeMessage = rule.activity.getString(R.string.auth_title)
        rule.onNodeWithText(welcomeMessage)
            .assertIsDisplayed()
    }
}