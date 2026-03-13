//Handler registration from state and Room-backed user creation
package com.example.auction6.ui.register

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.auction6.data.local.DatabaseProvider
import com.example.auction6.data.local.UserEntity
import kotlinx.coroutines.launch


@Composable
fun RegisterRoute(
    onRegisterSuccess: () -> Unit,
    onBackToLogin: () -> Unit,
    modifier: Modifier = Modifier
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val userDao = remember(context) {
        DatabaseProvider.get(context).userDao()
    }

    val isFormValid =
        email.isNotBlank() && password.isNotBlank() && confirmPassword.isNotBlank()

    val uiState = RegisterUiState(
        email = email,
        password = password,
        confirmPassword = confirmPassword,
        errorMessage = errorMessage,
        isRegisterEnabled = isFormValid
    )

    RegisterScreen(
        state = uiState,
        onEmailChange = { newEmail ->
            email = newEmail
            errorMessage = null
        },
        onPasswordChange = { newPassword ->
            password = newPassword
            errorMessage = null
        },
        onConfirmPasswordChange = { newConfirmPassword ->
            confirmPassword = newConfirmPassword
            errorMessage = null
        },
        onRegisterClick = {
            val normalizedEmail = email.trim()

            when {
                normalizedEmail.isBlank() || password.isBlank() || confirmPassword.isBlank() -> {
                    errorMessage = "Enter email and password"
                }
                password != confirmPassword -> {
                    errorMessage = "The passwords you entered do not match"
                }

                else -> {
                    coroutineScope.launch {
                        val existingUser = userDao.findByEmail(normalizedEmail)

                        if (existingUser != null) {
                            errorMessage = "An account with this Username already exists."
                        } else {
                            userDao.insert(
                                UserEntity(
                                    email = normalizedEmail,
                                    passwordHash = password,
                                    isVerified = false
                                )
                            )
                            errorMessage = null
                            onRegisterSuccess()
                        }
                    }
                }
            }
        },
        onBackToLoginClick = onBackToLogin,
        modifier = modifier
    )
}