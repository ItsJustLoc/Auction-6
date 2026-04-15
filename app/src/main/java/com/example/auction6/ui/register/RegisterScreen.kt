package com.example.auction6.ui.register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun RegisterScreen(
    state: RegisterUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onRegisterClick: () -> Unit,
    onBackToLoginClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = state.email,
            onValueChange = onEmailChange,
            label = { Text("Enter Email") }
        )

        TextField(
            value = state.password,
            onValueChange = onPasswordChange,
            label = { Text("Enter Password") }
        )

        TextField(
            value = state.confirmPassword,
            onValueChange = onConfirmPasswordChange,
            label = { Text("Confirm Password") }
        )

        Button(
            onClick = onRegisterClick,
            enabled = state.isRegisterEnabled
        ) {
            Text("Register")
        }
        Button(
            onClick = onBackToLoginClick,
        ) {
            Text("Back to Login")
        }

        state.errorMessage?.let { message ->
            Text(text = message)
        }
    }
}