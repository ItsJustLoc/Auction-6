package com.example.auction6.ui.verify


import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.auction6.data.local.DatabaseProvider
import kotlinx.coroutines.launch

/**
 * VerifyRoute — state owner and logic handler for the Verify screen.
 *
 * Responsibilities:
 *   1. Hold all mutable state (code input, error message).
 *   2. On first load, fetch the user's verification code and log it to logcat
 *      so the developer can find it during testing (replaces a real email service).
 *   3. On submit, compare the entered code against the stored code in Room.
 *   4. On match, mark the user as verified in the database and trigger navigation to Login.
 *   5. On mismatch or missing user, set an error message for VerifyScreen to display.
 *
 * This composable receives a userId from the nav graph (passed from RegisterRoute
 * or LoginRoute when an unverified user is detected). It never uses global state —
 * the userId is the only way to look up the correct user.
 *
 * Navigation contract:
 *   - Entered from: RegisterRoute (new user) or LoginRoute (returning unverified user)
 *   - Exits to:     LoginRoute via onVerifySuccess callback
 */
@Composable
fun VerifyRoute(
    userId: Long,               // The Room primary key of the user being verified.
    // Passed as a nav argument — see AppNavHost.
    onVerifySuccess: () -> Unit, // Called after isVerified = true is written to DB.
    // AppNavHost uses this to navigate to Login.
    onVerifyBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    // The 6-digit string the user is currently typing into the input field.
    var code by remember { mutableStateOf("") }

    // Null = no error shown. Set to a message string when verification fails.
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // Database access — scoped to this composable's lifetime via remember.
    val userDao = remember(context) { DatabaseProvider.get(context).userDao() }
    // Load the user's code into state so the screen can display it
    var devCode by remember { mutableStateOf<String?>(null) }
    // --- DEV HELPER ---
    // Runs once when this screen first loads (userId is the key).
    // Fetches the user from Room and prints their verification code to logcat.
    // Tag: Auction6_DEV — filter by this tag in Android Studio's Logcat.
    // In production, this block would be replaced with a real email/SMS send.
    LaunchedEffect(userId) {
        val user = userDao.findById(userId)
        if (user != null) {
            devCode = user.verificationCode
        }
    }

    // The Verify button is only enabled once exactly 6 characters have been entered.
    // This prevents submitting incomplete codes.
    val isSubmitEnabled = code.length == 6

    // Build the current UI state snapshot to pass down to VerifyScreen.
    val uiState = VerifyUiState(
        code = code,
        errorMessage = errorMessage,
        isSubmitEnabled = isSubmitEnabled,
        devCode = devCode // shown on screen during dev/demo
    )

    // Hand off rendering to VerifyScreen, passing state and callbacks.
    VerifyScreen(
        state = uiState,

        // Every keystroke: update the code state and clear any existing error
        // so the red error text disappears as soon as the user starts retyping.
        onCodeChange = { code = it; errorMessage = null },

        // Verify button tapped — run the check on a coroutine (Room requires off main thread).
        onSubmitClick = {
            coroutineScope.launch {
                val user = userDao.findById(userId)
                when {
                    // Edge case: user record missing from DB (shouldn't happen in normal flow).
                    user == null -> {
                        errorMessage = "Account not found. Please register again."
                    }

                    // Code doesn't match what was stored at registration.
                    user.verificationCode != code -> {
                        errorMessage = "Incorrect code. Please try again."
                    }

                    // Code matches — write isVerified = true to DB, then exit this screen.
                    // After this UPDATE, the user can log in and reach the Marketplace.
                    else -> {
                        userDao.setVerified(userId, true)
                        errorMessage = null
                        onVerifySuccess()
                    }
                }
            }
        },
        onBackToLoginClick = onVerifyBack,  // new callback
        modifier = modifier
    )
}