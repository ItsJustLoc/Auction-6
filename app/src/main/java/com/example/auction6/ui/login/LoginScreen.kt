package com.example.auction6.ui.login

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

// Stateless
@Composable
fun LoginScreen(
    state: LoginUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    Column(modifier = modifier) {

        // Email Text Field
        TextField(
            value = state.email,
            onValueChange = {
                    newEmail ->
                onEmailChange(newEmail)
            },
            label = { Text("Enter Email") }
        )

        // Password Text Field
        TextField(
            value = state.password,
            onValueChange = {
                    newPassword ->
                onPasswordChange(newPassword)
            },
            label = { Text("Enter Password") }
        )

        Button(onClick = onLoginClick, enabled = state.isLoginEnabled) {
            Text(text = "Login")
        }
        // if Error exist display under "Login" button
        state.errorMessage?.let { Text(it) }
    }
}