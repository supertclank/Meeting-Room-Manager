package com.example.meeting_room_manager.utils

import android.content.ContentValues
import android.content.Context
import android.content.SharedPreferences
import android.util.Base64
import android.util.Log
import com.auth0.android.jwt.JWT
import com.example.meeting_room_manager.activity.SettingsActivity
import org.json.JSONObject

object TokenUtils {

    private const val PREFS_NAME = "prefs"
    private const val TOKEN_KEY = "token"

    // Get the token from SharedPreferences
    fun getTokenFromStorage(context: Context): String? {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(TOKEN_KEY, null)
    }

    // Decode the JWT token to extract claims
    internal fun decodeTokenManually(token: String): Int? {
        return try {
            // JWT is in the format: header.payload.signature
            val parts = token.split(".")
            if (parts.size != 3) {
                Log.e("AppLog", "Invalid token format")
                return null
            }

            // The payload is the second part (index 1) and is Base64 encoded
            val payload = String(Base64.decode(parts[1], Base64.URL_SAFE))
            Log.d("AppLog", "Decoded JWT Payload: $payload")

            // Convert the payload into a JSONObject
            val jsonPayload = JSONObject(payload)

            // Extract the "id" field from the payload
            val userId = jsonPayload.getInt("id")
            Log.d("AppLog", "Extracted User ID: $userId")
            userId
        } catch (e: Exception) {
            Log.e("AppLog", "Failed to decode token: ${e.message}", e)
            null
        }
    }

    fun saveTokenToStorage(context: Context, token: String) {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(TOKEN_KEY, token)
        editor.apply()
    }

    fun getUserIdFromToken(token: String): Int {
        return try {
            val jwt = JWT(token)
            jwt.getClaim("id").asInt() ?: -1 // Return -1 if user ID not found
        } catch (e: Exception) {
            Log.e(ContentValues.TAG, "getUserIdFromToken: Error decoding token: ${e.message}")
            -1
        }
    }

    fun clearToken(settingsActivity: SettingsActivity) {
        val sharedPreferences: SharedPreferences =
            settingsActivity.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove(TOKEN_KEY)
        editor.apply()
    }

}
