package com.example.auction6.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.Modifier
import androidx.navigation.compose.composable
import com.example.auction6.ui.login.LoginRoute
import com.example.auction6.ui.marketplace.MarketplaceRoute

@Composable
fun AppNavHost(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Route.Login.route,
        modifier = modifier
    ) {
        // Login Route
        composable(Route.Login.route) {
            LoginRoute(
                onLoginSuccess = {
                    navController.navigate(Route.Marketplace.route) {
                        popUpTo(Route.Login.route) { inclusive = true }
                    }
                }
            )
        }

        // Marketplace Route
        composable(Route.Marketplace.route) {
            MarketplaceRoute(
                onLogoutSuccess = {
                    navController.navigate(Route.Login.route) {
                        popUpTo(Route.Marketplace.route) { inclusive = true }
                    }
                }
            )
        }


    }

}