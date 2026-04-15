// Owns login UI state and authenticates users with Room.
package com.example.auction6.ui.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import com.example.auction6.data.local.DatabaseProvider
import kotlinx.coroutines.launch

// State Owner
@Composable
fun LoginRoute(onLoginSuccess: (userId: Long) -> Unit,
               onGoToRegister: () -> Unit,
               onGoToVerify: (userId: Long) -> Unit,
               modifier: Modifier = Modifier)
{
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val userDao = remember(context) {
        DatabaseProvider.get(context).userDao()
    }
    // Enables/disables login button based on T/F of isFormValid
    val isFormValid = email.isNotBlank() && password.isNotBlank()

    // Creates internal LoginUiState data class
    val uiState = LoginUiState(
        email = email,
        password = password,
        errorMessage = errorMessage,
        isLoginEnabled = isFormValid
    )

    // Call to LoginScreen
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
            val normalizedEmail = email.trim()

            if (normalizedEmail.isBlank() || password.isBlank()) {
                errorMessage = "Enter email and password"
            } else {
                coroutineScope.launch {
                    val user = userDao.findByEmail(normalizedEmail)

                    when {
                        user == null -> {
                            errorMessage = "User not found"
                        }

                        user.passwordHash != password -> {
                            errorMessage = "Incorrect password"
                        }
                        !user.isVerified -> {
                            errorMessage = null
                            onGoToVerify(user.id)
                        }
                        else -> {
                            errorMessage = null
                            onLoginSuccess(user.id)
                        }
                    }
                }
            }
        },
        onGoToRegisterClick = onGoToRegister,
        modifier = modifier
    )
}