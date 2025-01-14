package com.example.meeting_room_manager.activity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import api.RetrofitClient
import api.data_class.EmailRecoveryRequest
import com.example.meeting_room_manager.ui.ForgotDetailsScreen
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForgotDetailsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ForgotDetailsScreen(
                onBackClick = { finish() }, // Close the activity
                onSubmitClick = { email ->
                    recoverUsername(email)
                }
            )

        }
    }

    private fun recoverUsername(email: String) {
        val recoveryRequest = EmailRecoveryRequest(email)
        val call = RetrofitClient.instance.recoverEmail(recoveryRequest)

        call.enqueue(object : Callback<Void> { // Adjust the response type based on your API
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(
                        this@ForgotDetailsActivity,
                        "Check your email for your username",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        this@ForgotDetailsActivity,
                        "Recovery failed",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@ForgotDetailsActivity, "Network error", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

}