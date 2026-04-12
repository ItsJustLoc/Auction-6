package com.example.auction6.ui.verify

/**
 * Represents the UI state for the Verify screen.
 *
 * This is a snapshot of everything the screen needs to render itself.
 * VerifyRoute owns and updates this state; VerifyScreen only reads it.
 */
data class VerifyUiState(
    val code: String = "",
    val errorMessage: String? = null,
    val isSubmitEnabled: Boolean = false,
    val devCode: String? = null //for showing Verification code during development
)