package com.example.meeting_room_manager.activity

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import com.example.meeting_room_manager.ui.UserScreen
import kotlinx.coroutines.launch

class UserActivity : BaseActivity() {

    override fun getScreenTitle(): String {
        return "User"
    }

    override fun setBackButton(): Boolean {
        return true
    }

    @Composable
    override fun ScreenContent() {
        val coroutineScope = rememberCoroutineScope()

        // Fetch greeting when the composable is displayed
        LaunchedEffect(Unit) {
            coroutineScope.launch {
            }
        }

        // Pass the greeting and interactions to DashboardScreen
        UserScreen(
            content = {

            },
        )
    }
}