package com.example.meeting_room_manager.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import api.ApiService
import api.data_class.UserPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SharedPreferenceManager(context: Context, private val apiService: ApiService) {

    private val PREFS_NAME = "UserPreferences"
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val KEY_USER_TOKEN = "user_token"
        private const val KEY_DARK_MODE_ENABLED = "dark_mode_enabled"
        private const val KEY_FONT_SIZE = "font_size"
    }

    // Getters
    private fun getUserToken(): String? {
        return sharedPreferences.getString(KEY_USER_TOKEN, null)
    }

    fun isDarkModeEnabled(): Boolean {
        return sharedPreferences.getBoolean(KEY_DARK_MODE_ENABLED, true)
    }

    fun getFontSize(): String? {
        return sharedPreferences.getString(KEY_FONT_SIZE, "Normal")
    }

    fun setDarkModeEnabled(enabled: Boolean) {
        sharedPreferences.edit().putBoolean(KEY_DARK_MODE_ENABLED, enabled).apply()
        syncPreferencesToServer() // Sync after changing preference
    }

    fun saveFontSize(fontSize: String) {
        sharedPreferences.edit().putString(KEY_FONT_SIZE, fontSize).apply()
        syncPreferencesToServer() // Sync after changing preference
    }

    // Sync preferences to the server
    private fun syncPreferencesToServer() {
        val token = getUserToken() ?: return
        val preferences = UserPreferences(
            darkMode = isDarkModeEnabled(),
            fontSize = getFontSize() ?: "Normal" // Default to "Normal" if null
        )

        apiService.updateUserPreferences("Bearer $token", preferences)
            .enqueue(object : Callback<UserPreferences> {
                override fun onResponse(
                    call: Call<UserPreferences>,
                    response: Response<UserPreferences>,
                ) {
                    if (response.isSuccessful) {
                        Log.d("Sync", "Preferences synced successfully: ${response.body()}")
                    } else {
                        Log.e(
                            "Sync",
                            "Failed to sync preferences: ${response.errorBody()?.string()}"
                        )
                    }
                }

                override fun onFailure(call: Call<UserPreferences>, t: Throwable) {
                    Log.e("Sync", "Error syncing preferences: ${t.message}")
                }
            })
    }
}