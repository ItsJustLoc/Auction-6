package com.example.auction6.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.auction6.ui.login.LoginRoute
import com.example.auction6.ui.marketplace.MarketplaceRoute
import com.example.auction6.ui.register.RegisterRoute

@Composable
fun AppNavHost(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Route.Login.route,
        modifier = modifier
    ) {
        composable(Route.Login.route) {
            LoginRoute(
                onLoginSuccess = {
                    navController.navigate(Route.Marketplace.route) {
                        popUpTo(Route.Login.route) { inclusive = true }
                    }
                },
                onGoToRegister = {
                    navController.navigate(Route.Register.route)
                }
            )
        }

        composable(Route.Register.route) {
            RegisterRoute(
                onRegisterSuccess = {
                    navController.navigate(Route.Login.route) {
                        popUpTo(Route.Register.route) { inclusive = true }
                    }
                },
                onBackToLogin = {
                    navController.popBackStack()
                }
            )
        }

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