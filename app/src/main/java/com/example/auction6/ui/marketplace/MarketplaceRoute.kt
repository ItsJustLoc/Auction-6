package com.example.auction6.ui.marketplace

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

// State Owner for Marketplace
@Composable
fun MarketplaceRoute(modifier: Modifier = Modifier, onLogoutSuccess: () -> Unit) {
    // call to MarketplaceScreen
    MarketplaceScreen(
        onLogoutClick = { onLogoutSuccess() },
        modifier = modifier
    )
}