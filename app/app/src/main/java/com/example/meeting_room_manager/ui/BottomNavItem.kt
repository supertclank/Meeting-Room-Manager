package com.example.meeting_room_manager.ui

import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem(
    val label: String,
    val icon: ImageVector,
    val onClick: () -> Unit,
)
