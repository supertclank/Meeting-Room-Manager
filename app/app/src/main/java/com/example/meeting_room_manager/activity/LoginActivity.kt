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
                onLoginClick = { username, password ->
                    loginVerify(username, password)
                },
                onForgotDetailsClick = {
                    startActivity(Intent(this, ForgotDetailsActivity::class.java))
                }
            )
        }
    }

    // Function to verify user login.
    private fun loginVerify(username: String, password: String) {

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, getString(R.string.enter_credentials), Toast.LENGTH_SHORT).show()
            return
        }

        sendLoginRequest(username, password) // Call the function to send login request
    }

    // Function to send the login request using OkHttp
    private fun sendLoginRequest(username: String, password: String) {
        val client = OkHttpClient()

        // Create the JSON body
        val json = """{"username":"$username", "password":"$password"}"""
        val requestBody = json.toRequestBody("application/json".toMediaType())

        val request = Request.Builder()
            .url("http://10.0.2.2:8000/login/")
            .post(requestBody)
            .build()

        // Execute the request asynchronously
        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                Log.e("LoginActivity", "Login failed: ${e.message}", e)
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
                    if (response.isSuccessful) {
                        val responseBody = response.body?.string()
                        val jsonResponse = JSONObject(responseBody)
                        val token = jsonResponse.getString("access_token")

                        // Decode the token and extract the user ID
                        val Id = decodeToken(token) ?: run {
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
                        Log.d("LoginActivity", "User ID: $Id")

                        runOnUiThread {
                            Toast.makeText(
                                this@LoginActivity,
                                "Login successful!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        val intent = Intent(this@LoginActivity, DashboardActivity::class.java)
                        intent.putExtra("USER_ID", Id)
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
                                    "Invalid username or password",
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
                    Log.e("LoginActivity", "Login failed: ${e.message}", e)
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
                return try {
                    val decodedToken = JWT(token)
                    decodedToken.getClaim("id").asInt() // Extract the user ID
                } catch (e: Exception) {
                    Log.e("LoginActivity", "Failed to decode token: ${e.message}", e)
                    null
                }
            }

            // Function to handle error responses
            private fun handleErrorResponse(response: okhttp3.Response) {
                val errorMessage = when (response.code) {
                    401 -> getString(R.string.incorrect_username_or_password)
                    404 -> getString(R.string.user_not_found)
                    else -> getString(R.string.login_failed_general)
                }

                runOnUiThread {
                    Toast.makeText(this@LoginActivity, errorMessage, Toast.LENGTH_SHORT).show()
                }
                Log.e("LoginActivity", "Login failed with status code: ${response.code}.")
            }
        })
    }
}