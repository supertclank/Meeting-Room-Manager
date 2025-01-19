package com.example.meeting_room_manager.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.meeting_room_manager.R

@Composable
fun ForgotDetailsScreen(
    onBackClick: () -> Unit,
    onSubmitClick: (String) -> Unit,
) {
    var email by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Title
            Text(
                text = stringResource(id = R.string.forgot_details_title),
                color = MaterialTheme.colorScheme.primary,
                fontSize = 24.sp,
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Email input field
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(text = stringResource(id = R.string.email)) },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.primary,
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Submit button
            Button(
                onClick = { onSubmitClick(email) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(id = R.string.submit))
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Back button
            TextButton(
                onClick = onBackClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(id = R.string.back),
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}
