package com.example.meeting_room_manager.utils

import android.content.ContentValues.TAG
import android.util.Log
import com.auth0.android.jwt.JWT

fun getUserIdFromToken(token: String): Int {
    return try {
        val jwt = JWT(token)
        jwt.getClaim("id").asInt() ?: -1 // Return -1 if user ID not found
    } catch (e: Exception) {
        Log.e(TAG, "getUserIdFromToken: Error decoding token: ${e.message}")
        -1
    }
}