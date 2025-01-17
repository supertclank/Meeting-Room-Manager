package com.example.meeting_room_manager.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.meeting_room_manager.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoomScreen(
    onCreateRoomClicked: () -> Unit,
    roomListContent: @Composable () -> Unit,
    availability: Boolean
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onCreateRoomClicked() },
                contentColor = MaterialTheme.colorScheme.onPrimary,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_add),
                    contentDescription = "Add Room"
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            roomListContent()

            Text(
                text = "Room Availability: ${if (availability) "Available" else "Not Available"}",
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}