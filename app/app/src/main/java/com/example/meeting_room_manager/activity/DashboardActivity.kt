package com.example.meeting_room_manager.activity

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import api.RetrofitClient
import api.data_class.UserRead
import com.example.meeting_room_manager.ui.DashboardScreen
import com.example.meeting_room_manager.utils.TokenUtils
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
                fetchGreeting { fetchedGreeting ->
                    Log.d("DashboardActivity", "Greeting fetched: $fetchedGreeting")
                    greeting = fetchedGreeting
                }
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


    private fun fetchGreeting(fetchedGreeting: (String) -> Unit) {
        val token = TokenUtils.getTokenFromStorage(this)
        if (token != null) {
            val userId = TokenUtils.decodeTokenManually(token)
            if (userId != null) {
                val apiService = RetrofitClient.instance
                Log.d("AppLog", "API call to fetch user with ID: $userId") // Log userId

                apiService.getUser(userId).enqueue(object : Callback<UserRead> {
                    override fun onResponse(call: Call<UserRead>, response: Response<UserRead>) {
                        Log.d("AppLog", "API Response: ${response.body()}")
                        if (response.isSuccessful) {
                            val firstName = response.body()?.first_name ?: "User"
                            fetchedGreeting("Hi, $firstName!")
                        } else {
                            Log.e(
                                "AppLog",
                                "Failed to fetch user data. Response code: ${response.code()}"
                            )
                            fetchedGreeting("Hi, Guest!")
                        }
                    }

                    override fun onFailure(call: Call<UserRead>, t: Throwable) {
                        Log.e("AppLog", "API call failed: ${t.message}")
                        fetchedGreeting("Hi, Guest!")
                    }
                })
            } else {
                fetchedGreeting("Hi, Guest!")
            }
        } else {
            fetchedGreeting("Hi, Guest!")
        }
    }
}
