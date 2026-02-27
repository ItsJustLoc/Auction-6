package com.example.auction6.ui.login

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

// Stateless
@Composable
fun LoginScreen(isLoggedIn: Boolean, onLoginClick: () -> Unit, modifier: Modifier = Modifier) {
    val statusText = if (isLoggedIn) "Logged in" else "Not logged in"

    Column(modifier = modifier) {
        Text(text =  statusText)
        Button(onClick = onLoginClick) {
            Text(text = if (isLoggedIn) "Log out" else "Login")
        }
    }
}