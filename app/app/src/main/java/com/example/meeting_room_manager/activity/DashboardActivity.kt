package com.example.meeting_room_manager.activity

import android.util.Log
import android.widget.TextView
import api.RetrofitClient
import api.data_class.UserRead
import com.example.meeting_room_manager.ui.DashboardScreen
import com.example.meeting_room_manager.utils.TokenUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DashboardActivity : BaseActivity(){
    private lateinit var greetingTextView: TextView

    setContent {
        DashboardScreen(
            title = getScreenTitle(),
            content = { ScreenContent() }
        )
    }

    // Display a greeting based on the user's first name
    private fun displayGreeting() {
        val token = TokenUtils.getTokenFromStorage(this)
        Log.d("AppLog", "Token: $token") // Log token to see if it's null or valid

        if (token != null) {
            val userId = TokenUtils.decodeTokenManually(token)
            Log.d("AppLog", "User ID: $userId") // Log the user ID

            if (userId != null) {
                Log.d("AppLog", "Fetching user data for userId: $userId")
                fetchUserFirstName(userId) // Pass userId directly
            } else {
                Log.e("AppLog", "User ID is null")
                greetingTextView.text = "Hi, Guest!"
            }
        } else {
            Log.e("AppLog", "Token is null")
            greetingTextView.text = "Hi, Guest!"
        }
    }

    private fun fetchUserFirstName(userId: Int) {
        val apiService = RetrofitClient.instance
        Log.d("AppLog", "API call to fetch user with ID: $userId") // Log userId

        apiService.getUser(userId = userId).enqueue(object : Callback<UserRead> {
            override fun onResponse(call: Call<UserRead>, response: Response<UserRead>) {
                Log.d("AppLog", "API Response: ${response.body()}")
                if (response.isSuccessful) {
                    val firstName = response.body()?.name ?: "User"
                    greetingTextView.text = "Hi, $firstName!"
                } else {
                    Log.e("AppLog", "Failed to fetch user data. Response code: ${response.code()}")
                    greetingTextView.text = "Hi, Guest!"
                }
            }

            override fun onFailure(call: Call<UserRead>, t: Throwable) {
                Log.e("AppLog", "API call failed: ${t.message}")
                greetingTextView.text = "Hi, Guest!"
            }
        })
    }
}