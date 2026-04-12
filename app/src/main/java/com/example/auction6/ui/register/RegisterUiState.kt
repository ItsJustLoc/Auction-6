package com.example.auction6.ui.register

data class RegisterUiState(
    val email: String ="",
    val password: String = "",
    val confirmPassword: String = "",
    val errorMessage: String? = null,
    val isRegisterEnabled: Boolean = false
)