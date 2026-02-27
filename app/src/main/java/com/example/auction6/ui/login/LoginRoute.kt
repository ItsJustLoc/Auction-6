package com.example.auction6.ui.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.auction6.ui.login.LoginScreen

// State Owner
@Composable
fun LoginRoute(modifier: Modifier = Modifier) {
    var isLoggedIn by remember { mutableStateOf(false) }

    LoginScreen(isLoggedIn = isLoggedIn, onLoginClick = {isLoggedIn = !isLoggedIn}, modifier = modifier)
}