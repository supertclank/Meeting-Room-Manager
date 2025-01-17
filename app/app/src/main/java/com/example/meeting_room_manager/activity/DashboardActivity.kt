package com.example.meeting_room_manager.activity

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import api.RetrofitClient
import com.example.meeting_room_manager.ui.DashboardScreen
import com.example.meeting_room_manager.utils.TokenUtils
import kotlinx.coroutines.launch

class DashboardActivity : BaseActivity() {

    override fun getScreenTitle(): String {
        return "Dashboard"
    }
    override fun setBackButton(): Boolean {
        return false
    }

    @Composable
    override fun ScreenContent() {
        var greeting by remember { mutableStateOf("Hi, Guest!") }
        val coroutineScope = rememberCoroutineScope()

        // Fetch greeting when the composable is displayed
        LaunchedEffect(Unit) {
            coroutineScope.launch {
                greeting = fetchGreeting()
            }
        }

        // Pass the greeting and interactions to DashboardScreen
        DashboardScreen(
            content = {
                // Display the greeting
                androidx.compose.material3.Text(text = greeting)
            },
        )
    }

    fun fetchGreeting(): String {
        val token = TokenUtils.getTokenFromStorage(this)
        if (token != null) {
            val userId = TokenUtils.decodeTokenManually(token)
            if (userId != null) {
                return try {
                    val response = RetrofitClient.instance.getUser(userId).execute()
                    if (response.isSuccessful) {
                        response.body()?.name?.let { "Hi, $it!" } ?: "Hi, Guest!"
                    } else {
                        "Hi, Guest!"
                    }
                } catch (e: Exception) {
                    "Hi, Guest!"
                }
            }
        }
        return "Hi, Guest!"
    }
}