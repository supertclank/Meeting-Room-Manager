package com.example.meeting_room_manager.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log // Import for logging
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Room
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import com.example.meeting_room_manager.ui.BaseScreen
import com.example.meeting_room_manager.ui.BottomNavItem

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
        finish()
    }
}