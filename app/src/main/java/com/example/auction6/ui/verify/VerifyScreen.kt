package com.example.auction6.ui.verify

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.dp

/**
 * Verify screen — pure UI, no logic.
 *
 * If you need to change how verification looks, edit here.
 * If you need to change how verification works, edit VerifyRoute.
 */
@Composable
fun VerifyScreen(
    state: VerifyUiState,           // Everything needed to render the screen
    onCodeChange: (String) -> Unit, // Called on every keystroke in the code field
    onSubmitClick: () -> Unit,
    onBackToLoginClick: () -> Unit, // Called when the user taps the Verify button
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Screen title
        Text(
            text = "Verify Your Account",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Dev-mode instruction — tells the tester where to find the code (logcat).
        // In a real app this would say "Check your email."
        Text(
            text = "Enter the 6-digit code shown in your logcat (dev mode).",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Code input field.
        // - keyboardType = Number opens the numeric keyboard on Android.
        // - isError turns the field outline red when an error message is present.
        // - onValueChange bubbles every keystroke up to VerifyRoute via the callback.
        OutlinedTextField(
            value = state.code,
            onValueChange = onCodeChange,
            label = { Text("Verification Code") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            isError = state.errorMessage != null,
            modifier = Modifier.fillMaxWidth()
        )

        // Error message — only rendered when errorMessage is non-null.
        // Examples: "Incorrect code", "Account not found"
        if (state.errorMessage != null) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = state.errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }
        // DEV ONLY — shows the code on screen so demo doesn't depend on Logcat.
        // Delete this block before any real release.
        if (state.devCode != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "DEV: Your code is ${state.devCode}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Submit button.
        // Disabled until isSubmitEnabled = true (i.e., exactly 6 digits entered).
        // Tapping this triggers the verification logic back in VerifyRoute.
        Button(
            onClick = onSubmitClick,
            enabled = state.isSubmitEnabled,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Verify")
        }
        Spacer(modifier = Modifier.height(8.dp))
        TextButton(onClick = onBackToLoginClick) {
            Text("Back to Login")
        }
    }
}