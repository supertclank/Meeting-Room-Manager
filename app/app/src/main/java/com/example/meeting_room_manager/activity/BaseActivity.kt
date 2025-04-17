package com.example.meeting_room_manager.activity

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Room
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import api.RetrofitClient
import com.example.meeting_room_manager.R
import com.example.meeting_room_manager.ui.BaseScreen
import com.example.meeting_room_manager.ui.BottomNavItem
import com.example.meeting_room_manager.utils.SharedPreferenceManager

abstract class BaseActivity : ComponentActivity() {

    private val tag = "BaseActivity" // Tag for logging

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(tag, "onCreate: BaseActivity is being created.") // Log activity creation

        setContent {
            Log.d(tag, "setContent: Setting up BaseScreen.") // Log before setting up BaseScreen
            BaseScreen(
                title = getScreenTitle(),
                showBackButton = setBackButton(),
                onBackButtonClicked = {
                    Log.d(tag, "onBackButtonClicked: Back button clicked.")
                    backButton()
                },
                content = {
                    Log.d(tag, "ScreenContent: Rendering content.") // Log before rendering content
                    ScreenContent()
                },
                bottomNavItems = listOf(
                    BottomNavItem("Home", Icons.Filled.Home) {
                        Log.d(tag, "onHomeClicked: Home button clicked.")
                        onHomeClicked()
                    },
                    BottomNavItem("Bookings", Icons.Filled.Book) {
                        Log.d(tag, "onBookingsClicked: Bookings button clicked.")
                        onBookingsClicked()
                    },
                    BottomNavItem("Rooms", Icons.Filled.Room) {
                        Log.d(tag, "onRoomsClicked: Rooms button clicked.")
                        onRoomsClicked()
                    },
                    BottomNavItem("Users", Icons.Filled.Person) {
                        Log.d(tag, "onUsersClicked: Users button clicked.")
                        onUsersClicked()
                    },
                    BottomNavItem("Settings", Icons.Filled.Settings) {
                        Log.d(tag, "onSettingsClicked: Settings button clicked.")
                        onSettingsClicked()
                    }
                )
            )
        }

        applyUserPreferences() // Apply user preferences
    }

    // Abstract function to be overridden by subclasses
    abstract fun getScreenTitle(): String

    abstract fun setBackButton(): Boolean

    @Composable
    abstract fun ScreenContent()

    // Handle the navigation logic
    open fun onHomeClicked() {
        Log.d(tag, "Navigating to DashboardActivity")
        startActivity(Intent(this, DashboardActivity::class.java))
    }

    open fun onBookingsClicked() {
        Log.d(tag, "Navigating to BookingsActivity")
        startActivity(Intent(this, BookingsActivity::class.java))
    }

    open fun onRoomsClicked() {
        Log.d(tag, "Navigating to RoomActivity")
        startActivity(Intent(this, RoomActivity::class.java))
    }

    open fun onUsersClicked() {
        Log.d(tag, "Navigating to UserActivity")
        startActivity(Intent(this, UserActivity::class.java))
    }

    open fun onSettingsClicked() {
        Log.d(tag, "Navigating to SettingsActivity")
        startActivity(Intent(this, SettingsActivity::class.java))
    }

    private fun backButton() {
        Log.d(tag, "BackButton clicked.")
        startActivity(Intent(this, DashboardActivity::class.java))
    }

    private fun applyUserPreferences() {
        val sharedPrefManager = SharedPreferenceManager(this, apiService = RetrofitClient.instance)

        val isDarkModeEnabled = sharedPrefManager.isDarkModeEnabled()
        setAppTheme(isDarkModeEnabled)

        val fontSize = sharedPrefManager.getFontSize() ?: "Normal"
        applyFontSize(fontSize)
    }

    private fun setAppTheme(isDarkModeEnabled: Boolean) {
        val theme =
            (
                    if (isDarkModeEnabled) {
                        R.style.Theme_MeetingRoomManager
                        Log.d(TAG, "Setting light theme")
                    } else {
                        R.style.Theme_MeetingRoomManager_Dark
                        Log.d(TAG, "Setting dark theme")
                    }
                    )
        setTheme(theme)
    }

    private fun applyFontSize(fontSize: String) {
        val configuration = resources.configuration
        Log.d(TAG, "Applying font size: $fontSize")
        val fontScale = when (fontSize) {
            "Small" -> 0.85f
            "Large" -> 1.15f
            else -> 1.0f // Default or "Normal"
        }
        configuration.fontScale = fontScale
        Log.d(TAG, "Font scale applied: $fontScale")
        resources.updateConfiguration(configuration, resources.displayMetrics)
    }
}