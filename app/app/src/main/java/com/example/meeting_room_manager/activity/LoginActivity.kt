package com.example.meeting_room_manager.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.auth0.android.jwt.JWT
import com.example.meeting_room_manager.R
import com.example.meeting_room_manager.ui.LoginScreen
import com.example.meeting_room_manager.utils.TokenUtils
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LoginScreen(
                onLoginClick = { email, password ->
                    Log.d("LoginActivity", "Login attempt with email: $email")
                    loginVerify(email, password)
                },
                onForgotDetailsClick = {
                    Log.d("LoginActivity", "Forgot details clicked")
                    startActivity(Intent(this, ForgotDetailsActivity::class.java))
                }
            )
        }
    }

    // Function to verify user login.
    private fun loginVerify(email: String, password: String) {
        Log.d("LoginActivity", "Verifying credentials: email = $email, password = $password")

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, getString(R.string.enter_credentials), Toast.LENGTH_SHORT).show()
            Log.d("LoginActivity", "Email or password is empty")
            return
        }

        sendLoginRequest(email, password) // Call the function to send login request
    }

    // Function to send the login request using OkHttp
    private fun sendLoginRequest(email: String, password: String) {
        Log.d("LoginActivity", "Sending login request for email: $email")

        val client = OkHttpClient()

        // Create the JSON body
        val json = """{"email":"$email", "password":"$password"}"""
        val requestBody = json.toRequestBody("application/json".toMediaType())

        val request = Request.Builder()
            .url("http://10.0.2.2:8000/login/")
            .post(requestBody)
            .build()

        // Execute the request asynchronously
        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                Log.e("LoginActivity", "Login request failed: ${e.message}", e)
                runOnUiThread {
                    Toast.makeText(
                        this@LoginActivity,
                        "Login failed: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                try {
                    Log.d("LoginActivity", "Received response with status code: ${response.code}")

                    if (response.isSuccessful) {
                        val responseBody = response.body?.string()
                        Log.d("LoginActivity", "Response body: $responseBody")

                        val jsonResponse = JSONObject(responseBody.toString())
                        val token = jsonResponse.getString("access_token")

                        Log.d("LoginActivity", "Access token: $token")

                        // Decode the token and extract the user ID
                        val id = decodeToken(token) ?: run {
                            Log.e("LoginActivity", "Failed to decode token, user ID is null")
                            runOnUiThread {
                                Toast.makeText(
                                    this@LoginActivity,
                                    "Failed to retrieve user ID.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            return // Exit the method if user ID cannot be retrieved
                        }

                        TokenUtils.saveTokenToStorage(this@LoginActivity, token)

                        Log.d("LoginActivity", "Login successful: $responseBody")
                        Log.d("LoginActivity", "User ID: $id")

                        runOnUiThread {
                            Toast.makeText(
                                this@LoginActivity,
                                "Login successful!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        val intent = Intent(this@LoginActivity, DashboardActivity::class.java)
                        intent.putExtra("USER_ID", id)
                        startActivity(intent)
                        finish()

                    } else {
                        Log.e(
                            "LoginActivity",
                            "Login failed with status code: ${response.code}. Response body: ${response.body?.string()}"
                        )
                        runOnUiThread {
                            when (response.code) {
                                401 -> Toast.makeText(
                                    this@LoginActivity,
                                    "Invalid email or password",
                                    Toast.LENGTH_SHORT
                                ).show()

                                else -> Toast.makeText(
                                    this@LoginActivity,
                                    "Login failed. Please try again.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                } catch (e: Exception) {
                    Log.e("LoginActivity", "Login request exception: ${e.message}", e)
                    runOnUiThread {
                        Toast.makeText(
                            this@LoginActivity,
                            "Login failed: ${e.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            // Function to decode the token and extract the user ID
            private fun decodeToken(token: String): Int? {
                Log.d("LoginActivity", "Decoding token: $token")
                return try {
                    val decodedToken = JWT(token)
                    val userId = decodedToken.getClaim("id").asInt()
                    Log.d("LoginActivity", "Decoded user ID: $userId")
                    userId
                } catch (e: Exception) {
                    Log.e("LoginActivity", "Failed to decode token: ${e.message}", e)
                    null
                }
            }
        })
    }
}