package com.example.auction6.ui.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

// State Owner
@Composable
fun LoginRoute(modifier: Modifier = Modifier) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Enables/disables login button based on T/F of isFormValid
    val isFormValid = email.isNotBlank() && password.isNotBlank()

    val uiState = LoginUiState(
        email = email,
        password = password,
        errorMessage = errorMessage,
        isLoginEnabled = isFormValid
    )

    LoginScreen(
        state = uiState,
        onEmailChange = { newEmail ->
            email = newEmail
            errorMessage = null
        },
        onPasswordChange = { newPassword ->
            password = newPassword
            errorMessage = null
        },
        onLoginClick = {
            if (isFormValid)
                errorMessage = null
            else
                errorMessage = "Enter email and password" },
        modifier = modifier
    )
}