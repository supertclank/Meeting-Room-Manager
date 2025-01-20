package com.example.meeting_room_manager.activity

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import api.RetrofitClient
import api.data_class.RoomCreate
import api.data_class.RoomRead
import api.data_class.UserRead
import com.example.meeting_room_manager.ui.AddRoomDialog
import com.example.meeting_room_manager.ui.RoomScreen
import com.example.meeting_room_manager.utils.TokenUtils
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RoomActivity : BaseActivity() {

    private lateinit var token: String

    override fun getScreenTitle(): String {
        return "Room"
    }

    override fun setBackButton(): Boolean {
        return true
    }

    @Composable
    override fun ScreenContent() {
        val coroutineScope = rememberCoroutineScope()
        var showAddRoomDialog by remember { mutableStateOf(false) }
        var roomList by remember { mutableStateOf<List<RoomRead>>(emptyList()) }
        var isAdmin by remember { mutableStateOf(false) }
        var user by remember { mutableStateOf<UserRead?>(null) }

        LaunchedEffect(Unit) {
            token = TokenUtils.getTokenFromStorage(this@RoomActivity) ?: run {
                Log.e("RoomActivity", "Token is null")
                Toast.makeText(this@RoomActivity, "Error: No valid token", Toast.LENGTH_SHORT)
                    .show()
                return@LaunchedEffect
            }

            coroutineScope.launch {
                try {
                    val apiService = RetrofitClient.instance
                    val userId = TokenUtils.getUserIdFromToken(token)  // Get user ID from token

                    if (userId != -1) {
                        apiService.getUser(userId).enqueue(object : Callback<UserRead> {
                            override fun onResponse(
                                call: Call<UserRead>,
                                response: Response<UserRead>
                            ) {
                                if (response.isSuccessful) {
                                    user = response.body()!!
                                    isAdmin = user?.user_type == "admin"
                                } else {
                                    Log.e(
                                        "RoomActivity",
                                        "Error fetching user details: ${response.message()}"
                                    )
                                }
                            }

                            override fun onFailure(call: Call<UserRead>, t: Throwable) {
                                Log.e(
                                    "RoomActivity",
                                    "Exception fetching user details: ${t.message}"
                                )
                            }
                        })
                    } else {
                        Log.e("RoomActivity", "Failed to decode token and retrieve user ID")
                    }

                    val response = RetrofitClient.instance.getRooms("Bearer $token")
                    response.enqueue(object : Callback<List<RoomRead>> {
                        override fun onResponse(
                            call: Call<List<RoomRead>>,
                            response: Response<List<RoomRead>>
                        ) {
                            if (response.isSuccessful) {
                                roomList = response.body() ?: emptyList()
                            } else {
                                Log.e(
                                    "RoomActivity",
                                    "Error fetching room list: ${response.message()}"
                                )
                            }
                        }

                        override fun onFailure(call: Call<List<RoomRead>>, t: Throwable) {
                            Log.e("RoomActivity", "Exception fetching room list: ${t.message}")
                        }
                    })
                } catch (e: Exception) {
                    Log.e("RoomActivity", "Exception fetching room list: ${e.message}")
                }
            }
        }

        RoomScreen(
            onCreateRoomClicked = {
                showAddRoomDialog = true
            },
            roomListContent = {
                DisplayRoomList(roomList)
            },
            availability = roomList.any { it.availability }
        )

        if (showAddRoomDialog) {
            AddRoomDialog(
                onDismissRequest = { showAddRoomDialog = false },
                onRoomAdded = { name, capacity, amenities ->
                    coroutineScope.launch {
                        createNewRoom(token, name, capacity, amenities)
                        showAddRoomDialog = false
                        // Fetch updated room list after adding a new room
                        try {
                            val response = RetrofitClient.instance.getRooms("Bearer $token")
                            response.enqueue(object : Callback<List<RoomRead>> {
                                override fun onResponse(
                                    call: Call<List<RoomRead>>,
                                    response: Response<List<RoomRead>>
                                ) {
                                    if (response.isSuccessful) {
                                        roomList = response.body() ?: emptyList()
                                    } else {
                                        Log.e(
                                            "RoomActivity",
                                            "Error fetching room list: ${response.message()}"
                                        )
                                    }
                                }

                                override fun onFailure(call: Call<List<RoomRead>>, t: Throwable) {
                                    Log.e(
                                        "RoomActivity",
                                        "Exception fetching room list: ${t.message}"
                                    )
                                }
                            })
                        } catch (e: Exception) {
                            Log.e("RoomActivity", "Exception fetching room list: ${e.message}")
                        }
                    }
                }
            )
        }
    }

    @Composable
    private fun DisplayRoomList(roomList: List<RoomRead>) {
        LazyColumn(modifier = Modifier.padding(16.dp)) {
            items(roomList) { room ->
                RoomItem(room = room)
            }
        }
    }

    @Composable
    fun RoomItem(room: RoomRead) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Room Name: ${room.name}", fontWeight = FontWeight.Bold)
                Text(text = "Capacity: ${room.capacity}")
                Text(text = "Amenities: ${room.amenities}")
                Text(text = "Availability: ${if (room.availability) "Available" else "Not Available"}")
            }
        }
    }

    private fun createNewRoom(token: String, name: String, capacity: Int, amenities: String?) {
        try {
            val newRoom = RoomCreate(
                name = name,
                capacity = capacity,
                amenities = amenities,
                availability = true
            )

            val response = RetrofitClient.instance.createNewRoom("Bearer $token", newRoom)
            response.enqueue(object : Callback<RoomRead> {
                override fun onResponse(call: Call<RoomRead>, response: Response<RoomRead>) {
                    if (response.isSuccessful) {
                        Log.d("RoomActivity", "Room successfully created: ${response.body()}")
                    } else {
                        Log.e("RoomActivity", "Error creating room: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<RoomRead>, t: Throwable) {
                    Log.e("RoomActivity", "Exception creating room: ${t.message}")
                }
            })
        } catch (e: Exception) {
            Log.e("RoomActivity", "Exception creating room: ${e.message}")
        }
    }
}