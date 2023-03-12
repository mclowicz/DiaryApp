package com.example.diaryapp.home

import android.content.res.Resources
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDrawerState
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.onNodeWithText
import com.example.diaryapp.R
import com.example.diaryapp.presentation.screens.home.HomeScreen
import com.example.diaryapp.ui.theme.DiaryAppTheme

@ExperimentalMaterial3Api
@ExperimentalAnimationApi
fun launchHomeScreen(
    rule: ComposeContentTestRule,
    resources: Resources,
    block: HomeRobot.() -> Unit
): HomeRobot {
    rule.setContent {
        DiaryAppTheme {
            val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
            HomeScreen(
                drawerState = drawerState,
                onMenuClick = { /*TODO*/ },
                onSignOutClicked = {  },
                navigateToWrite = {  }
            )
        }
    }
    return HomeRobot(rule, resources).apply(block)
}

@ExperimentalMaterial3Api
@ExperimentalAnimationApi
class HomeRobot(
    private val rule: ComposeContentTestRule,
    private val resources: Resources,
) {

    infix fun verify(block: HomeVerification.() -> Unit): HomeVerification {
        return HomeVerification(rule, resources).apply(block)
    }
}

@ExperimentalMaterial3Api
@ExperimentalAnimationApi
class HomeVerification(
    private val rule: ComposeContentTestRule,
    private val resources: Resources
) {
    fun titleIsVisible() {
        val title = resources.getString(R.string.home_title)
        rule.onNodeWithText(title)
            .assertIsDisplayed()
    }
}