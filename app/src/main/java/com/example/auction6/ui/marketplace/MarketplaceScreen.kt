package com.example.auction6.ui.marketplace

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

// Stateless UI for marketplace
@Composable
fun MarketplaceScreen(
    onLogoutClick: () -> Unit,
    modifier: Modifier = Modifier
) {


    Column(modifier = modifier) {
        Text("Marketplace ... Coming Soon")

        // Log out button
        Button(onClick = onLogoutClick) {
            Text(text = "Log out")
        }
    }

}