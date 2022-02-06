package com.homework.project.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.homework.project.ReminderAppState
import com.homework.project.rememberReminderAppState
import com.homework.project.ui.login.Login
import com.homework.project.ui.reminders.RemindersView
import com.homework.project.ui.userProfile.UserProfile

@Composable
fun ReminderApp(appState: ReminderAppState = rememberReminderAppState()) {
    NavHost(
        navController = appState.navController,
        startDestination = "login"
    ) {
        composable(route = "login") {
            Login(navController = appState.navController)
        }
        composable(route = "reminders") {
            RemindersView(navController = appState.navController)
        }
        composable(route = "profile") {
            UserProfile(onBackPress = appState::navigateBack)
        }
    }
}