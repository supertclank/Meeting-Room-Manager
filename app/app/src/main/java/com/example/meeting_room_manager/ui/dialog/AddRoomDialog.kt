package com.example.meeting_room_manager.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun AddRoomDialog(
    onDismissRequest: () -> Unit,
    onRoomAdded: (String, Int, String?) -> Unit, // Changed to include amenities
) {
    var roomName by remember { mutableStateOf("") }
    var roomCapacity by remember { mutableStateOf("") }
    var isRoomCapacityValid by remember { mutableStateOf(true) }
    val allAmenities = listOf("Projector", "Whiteboard", "Conference Call", "Refreshments")
    val selectedAmenities = remember { mutableStateListOf<String>() }

    AlertDialog(
        onDismissRequest = {
            onDismissRequest()
        },
        title = {
            Text(text = "Add New Room")
        },
        text = {
            Column(modifier = Modifier.padding(8.dp)) {
                OutlinedTextField(
                    value = roomName,
                    onValueChange = { roomName = it },
                    label = { Text("Room Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = roomCapacity,
                    onValueChange = {
                        if (it.all { char -> char.isDigit() } || it.isBlank()) {
                            roomCapacity = it
                            isRoomCapacityValid = true
                        } else {
                            isRoomCapacityValid = false
                        }
                    },
                    label = { Text("Room Capacity") },
                    modifier = Modifier.fillMaxWidth()
                )
                if (!isRoomCapacityValid) {
                    Text("Room Capacity must be a number")
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text("Select Amenities:")
                Column {
                    allAmenities.forEach { amenity ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(
                                checked = selectedAmenities.contains(amenity),
                                onCheckedChange = { isChecked ->
                                    if (isChecked) {
                                        selectedAmenities.add(amenity)
                                    } else {
                                        selectedAmenities.remove(amenity)
                                    }
                                }
                            )
                            Text(text = amenity)
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val capacity = roomCapacity.toIntOrNull() ?: 0
                    if (roomName.isNotBlank() && capacity > 0 && isRoomCapacityValid) {
                        val amenitiesString =
                            if (selectedAmenities.isNotEmpty()) selectedAmenities.joinToString(", ") else null
                        onRoomAdded(roomName, capacity, amenitiesString)
                    }
                }
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text("Cancel")
            }
        }
    )
}