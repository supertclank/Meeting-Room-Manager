package com.example.meeting_room_manager.activity

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.meeting_room_manager.ui.AddRoomDialog
import com.example.meeting_room_manager.ui.RoomScreen
import kotlinx.coroutines.launch
import api.data_class.RoomCreate
import api.data_class.RoomRead
import api.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RoomActivity : BaseActivity() {

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

        LaunchedEffect(Unit) {
            coroutineScope.launch {
                try {
                    val response = RetrofitClient.instance.getRooms() // Assuming a method to get the list of rooms
                    response.enqueue(object : Callback<List<RoomRead>> {
                        override fun onResponse(call: Call<List<RoomRead>>, response: Response<List<RoomRead>>) {
                            if (response.isSuccessful) {
                                roomList = response.body() ?: emptyList()
                            } else {
                                Log.e("RoomActivity", "Error fetching room list: ${response.message()}")
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
                        createNewRoom(name, capacity, amenities)
                        showAddRoomDialog = false
                        // Fetch updated room list after adding a new room
                        try {
                            val response = RetrofitClient.instance.getRooms()
                            response.enqueue(object : Callback<List<RoomRead>> {
                                override fun onResponse(call: Call<List<RoomRead>>, response: Response<List<RoomRead>>) {
                                    if (response.isSuccessful) {
                                        roomList = response.body() ?: emptyList()
                                    } else {
                                        Log.e("RoomActivity", "Error fetching room list: ${response.message()}")
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
                Text(text = "Amenities: ${room.amenities ?: "None"}")
                Text(text = "Availability: ${if (room.availability) "Available" else "Not Available"}")
            }
        }
    }

    private suspend fun createNewRoom(name: String, capacity: Int, amenities: String?) {
        try {
            val newRoom = RoomCreate(name = name, capacity = capacity, amenities = amenities, availability = true)
            val response = RetrofitClient.instance.createNewRoom(0, newRoom) // Assume 0 as dummy room ID
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