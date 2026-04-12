//Only display UI and emit events
package com.example.auction6.ui.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// Stateless
@Composable
fun LoginScreen(
    state: LoginUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onGoToRegisterClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

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

        // Login button (greyed out when email / password is blank)
        Button(onClick = onLoginClick, enabled = state.isLoginEnabled) {
            Text(text = "Login")
        }
        //Register button (greyed out when email / password is blank)
        Button(onClick = onGoToRegisterClick) {
            Text(text = "Create Account")
        }
        // if Error exist display under "Login" button
        state.errorMessage?.let { Text(it) }
    }
}