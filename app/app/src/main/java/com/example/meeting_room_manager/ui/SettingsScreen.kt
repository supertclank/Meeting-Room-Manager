package com.example.meeting_room_manager.ui

import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import api.data_class.UserRead
import com.example.meeting_room_manager.activity.SettingsActivity
import com.example.meeting_room_manager.utils.SharedPreferenceManager

@Composable
fun SettingsScreen(
    token: String,
    sharedPreferenceManager: SharedPreferenceManager,
    onLogout: () -> Unit,
) {
    var darkModeEnabled by remember { mutableStateOf(sharedPreferenceManager.isDarkModeEnabled()) }
    var fontSize by remember { mutableStateOf(sharedPreferenceManager.getFontSize() ?: "Normal") }
    var userDetails by remember { mutableStateOf<UserRead?>(null) }
    var isDialogVisible by remember { mutableStateOf(false) } // Track dialog visibility

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Logout button
        Button(onClick = onLogout) {
            Text("Logout")
        }

        // Edit Account Details button (shows the dialog)
        userDetails?.let { user ->
            Button(onClick = { isDialogVisible = true }) {
                Text("Edit Account Details")
            }
        }

        // Show dialog conditionally
        if (isDialogVisible) {
            userDetails?.let { user ->
                ShowEditUserDialog(
                    user = user,
                    token = token,
                    onSave = { updatedUser ->
                        // Update user details logic here
                        userDetails = updatedUser // Update local state with new details
                        isDialogVisible = false   // Close the dialog
                    },
                    onDismiss = {
                        isDialogVisible = false   // Close the dialog
                    }
                )
            }
        }

        // Toggle Dark Mode
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Dark Mode")
            Switch(
                checked = darkModeEnabled,
                onCheckedChange = {
                    darkModeEnabled = it
                    sharedPreferenceManager.setDarkModeEnabled(it)
                    Log.d("SettingsScreen", "Dark Mode Toggled: $it")
                    
                }
            )
        }

        // Font Size Dropdown
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Font Size")
            Spacer(modifier = Modifier.width(16.dp))
            FontSizeDropdown(
                selectedFontSize = fontSize,
                onFontSizeSelected = {
                    fontSize = it
                    sharedPreferenceManager.saveFontSize(it)
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FontSizeDropdown(
    selectedFontSize: String,
    onFontSizeSelected: (String) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) } // Tracks if the dropdown is visible
    val fontSizeOptions = listOf("Small", "Normal", "Large")

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        OutlinedTextField(
            value = selectedFontSize,
            onValueChange = {},
            label = { Text("Font Size") },
            readOnly = true,
            modifier = Modifier.menuAnchor(), // Ensures proper positioning of the menu
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            singleLine = true
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            fontSizeOptions.forEach { fontSize ->
                DropdownMenuItem(
                    text = { Text(fontSize) },
                    onClick = {
                        onFontSizeSelected(fontSize) // Callback for when a font size is selected
                        expanded = false
                    }
                )
            }
        }
    }
}