package com.example.meeting_room_manager.activity

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import com.example.meeting_room_manager.ui.BookingsScreen
import kotlinx.coroutines.launch

class BookingsActivity : BaseActivity() {

    override fun getScreenTitle(): String {
        return "Bookings"
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
        BookingsScreen(
            content = {

            },
        )
    }
}