package com.example.meeting_room_manager.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import com.example.meeting_room_manager.ui.BaseScreen

abstract class BaseActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
                BaseScreen(
                    title = getScreenTitle(),
                    content = { ScreenContent() }
                )
        }
    }

    abstract fun getScreenTitle(): String
    @Composable
    abstract fun ScreenContent()
}