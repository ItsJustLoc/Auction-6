package com.example.auction6.ui.login

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val errorMessage: String? = null,
    val isLoginEnabled: Boolean = false
)