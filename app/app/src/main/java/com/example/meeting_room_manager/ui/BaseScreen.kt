package com.example.meeting_room_manager.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseScreen(
    title: String,
    content: @Composable () -> Unit,
    bottomNavItems: List<BottomNavItem> = emptyList(),
    showBackButton: Boolean = true,
    onBackButtonClicked: () -> Unit = {}, // Default no-op function
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = title,
                        modifier = Modifier,
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    if (showBackButton) {
                        IconButton(onClick = onBackButtonClicked) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = "Navigate back")
                        }
                    } else {
                        null
                    }
                }
            )

        },
        bottomBar = {
            BottomAppBar(
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    bottomNavItems.forEach { navItem ->
                        IconButton(onClick = navItem.onClick) {
                            Icon(navItem.icon, contentDescription = navItem.label)
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            content()
        }
    }
}