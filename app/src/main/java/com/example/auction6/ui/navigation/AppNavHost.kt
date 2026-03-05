package com.example.auction6.ui.navigation

import androidx.compose.ui.Modifier
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.auction6.ui.login.LoginRoute
import com.example.auction6.ui.marketplace.MarketplaceScreen // or MarketplaceRoute

@Composable
fun AppNavHost(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.LOGIN,
        modifier = modifier
    ) {
        composable(Routes.LOGIN) {
            LoginRoute(
                onLoginSuccess = { navController.navigate(Routes.MARKETPLACE) }
            )
        }

        composable(Routes.MARKETPLACE) {
            MarketplaceScreen()
        }
    }
}