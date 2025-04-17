package com.example.meeting_room_manager.activity

import android.content.ContentValues.TAG
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import api.RetrofitClient
import api.data_class.UserRead
import com.example.meeting_room_manager.ui.SettingsScreen
import com.example.meeting_room_manager.utils.SharedPreferenceManager
import com.example.meeting_room_manager.utils.TokenUtils
import com.example.meeting_room_manager.utils.getUserIdFromToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SettingsActivity : BaseActivity() {

    private lateinit var token: String

    override fun getScreenTitle(): String {
        return "Settings"
    }

    override fun setBackButton(): Boolean {
        return true
    }

    @Composable
    override fun ScreenContent() {
        // Create a shared preference manager
        val sharedPreferenceManager = remember {
            SharedPreferenceManager(this, apiService = RetrofitClient.instance)
        }

        // Mutable states to manage user data and loading state
        var user by remember { mutableStateOf<UserRead?>(null) }
        var isLoading by remember { mutableStateOf(true) }

        // Fetch user details in a LaunchedEffect
        LaunchedEffect(Unit) {
            token = TokenUtils.getTokenFromStorage(this@SettingsActivity) ?: run {
                Log.e("SettingsActivity", "Token is null")
                Toast.makeText(this@SettingsActivity, "Error: No valid token", Toast.LENGTH_SHORT)
                    .show()
                finish()
                return@LaunchedEffect
            }

            val userId = getUserIdFromToken(token)
            RetrofitClient.instance.getUser(userId).enqueue(object : Callback<UserRead> {
                override fun onResponse(call: Call<UserRead>, response: Response<UserRead>) {
                    if (response.isSuccessful) {
                        user = response.body()
                    } else {
                        Log.e(
                            "SettingsActivity",
                            "Failed to fetch user details: ${response.code()}"
                        )
                    }
                    isLoading = false
                }

                override fun onFailure(call: Call<UserRead>, t: Throwable) {
                    Log.e("SettingsActivity", "Error fetching user details", t)
                    isLoading = false
                }
            })
        }

        if (isLoading) {
            androidx.compose.material3.CircularProgressIndicator(
                modifier = Modifier.padding(16.dp)
            )
        } else {
            user?.let {
                SettingsScreen(
                    token = token,
                    sharedPreferenceManager = sharedPreferenceManager,
                    onLogout = {
                        TokenUtils.clearToken(this@SettingsActivity)
                        startActivity(Intent(this@SettingsActivity, LoginActivity::class.java))
                        finish()
                    }
                )
            } ?: Text("Failed to load user details", modifier = Modifier.padding(16.dp))
        }

    }


    // Utility function to load user details from the API
    private fun loadUserDetails(token: String, onSuccess: (UserRead) -> Unit) {
        val userId = getUserIdFromToken(token)

        RetrofitClient.instance.getUser(userId).enqueue(object : Callback<UserRead> {
            override fun onResponse(call: Call<UserRead>, response: Response<UserRead>) {
                if (response.isSuccessful) {
                    response.body()?.let(onSuccess)
                } else {
                    Log.e(TAG, "Failed to load user details: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<UserRead>, t: Throwable) {
                Log.e(TAG, "Error loading user details", t)
            }
        })
    }
}