package com.example.auction6.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.auction6.ui.bid.PlaceBidRoute
import com.example.auction6.ui.create_listing.CreateListingRoute
import com.example.auction6.ui.history.BuyerHistoryRoute
import com.example.auction6.ui.history.SellerHistoryRoute
import com.example.auction6.ui.login.LoginRoute
import com.example.auction6.ui.marketplace.ListingDetailRoute
import com.example.auction6.ui.marketplace.MarketplaceRoute
import com.example.auction6.ui.register.RegisterRoute
import com.example.auction6.ui.verify.VerifyRoute

@Composable
fun AppNavHost(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    var currentUserId by remember { mutableStateOf(0L) }
    var marketplaceRefresh by remember { mutableStateOf(0) }
    var listingDetailRefresh by remember { mutableStateOf(0) }

    NavHost(
        navController = navController,
        startDestination = Route.Login.route,
        modifier = modifier
    ) {
        composable(Route.Login.route) {
            LoginRoute(
                onLoginSuccess = { userId ->
                    currentUserId = userId
                    navController.navigate(Route.Marketplace.route) {
                        popUpTo(Route.Login.route) { inclusive = true }
                    }
                },
                onGoToRegister = {
                    navController.navigate(Route.Register.route)
                },
                onGoToVerify = { userId ->
                    navController.navigate(Route.Verify.createRoute(userId)) {
                        popUpTo(Route.Login.route) { inclusive = false }
                    }
                }
            )
        }

        composable(Route.Register.route) {
            RegisterRoute(
                onRegisterSuccess = { userId ->
                    navController.navigate(Route.Verify.createRoute(userId)) {
                        popUpTo(Route.Register.route) { inclusive = true }
                    }
                },
                onBackToLogin = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Route.Verify.route,
            arguments = listOf(navArgument("userId") { type = NavType.LongType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getLong("userId") ?: return@composable
            VerifyRoute(
                userId = userId,
                onVerifySuccess = {
                    navController.navigate(Route.Login.route) {
                        popUpTo(Route.Verify.route) { inclusive = true }
                    }
                },
                onVerifyBack = {
                    navController.popBackStack()  // <-- add this
                }
            )
        }

        composable(Route.Marketplace.route) {
            MarketplaceRoute(
                currentUserId = currentUserId,
                refreshTrigger = marketplaceRefresh,
                onLogoutSuccess = {
                    navController.navigate(Route.Login.route) {
                        popUpTo(Route.Marketplace.route) { inclusive = true }
                    }
                },
                onListingClick = { listingId ->
                    navController.navigate(Route.ListingDetail.createRoute(listingId))
                },
                onCreateListingClick = {
                    navController.navigate(Route.CreateListing.route)
                },
                onBuyerHistoryClick = {
                    navController.navigate(Route.BuyerHistory.route)
                },
                onSellerHistoryClick = {
                    navController.navigate(Route.SellerHistory.route)
                }
            )
        }

        composable(Route.CreateListing.route) {
            CreateListingRoute(
                currentUserId = currentUserId,
                onSaved = {
                    marketplaceRefresh++
                    navController.popBackStack()
                },
                onCancel = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Route.ListingDetail.route,
            arguments = listOf(navArgument("listingId") { type = NavType.IntType })
        ) { backStackEntry ->
            val listingId = backStackEntry.arguments?.getInt("listingId") ?: return@composable
            ListingDetailRoute(
                listingId = listingId,
                currentUserId = currentUserId,
                refreshTrigger = listingDetailRefresh,
                onBack = { navController.popBackStack() },
                onPlaceBidClick = {
                    navController.navigate(Route.PlaceBid.createRoute(listingId))
                }
            )
        }

        composable(Route.BuyerHistory.route) {
            BuyerHistoryRoute(
                currentUserId = currentUserId,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Route.SellerHistory.route) {
            SellerHistoryRoute(
                currentUserId = currentUserId,
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Route.PlaceBid.route,
            arguments = listOf(navArgument("listingId") { type = NavType.IntType })
        ) { backStackEntry ->
            val listingId = backStackEntry.arguments?.getInt("listingId") ?: return@composable
            PlaceBidRoute(
                listingId = listingId,
                currentUserId = currentUserId,
                onBack = {
                    listingDetailRefresh++
                    navController.popBackStack()
                }
            )
        }
    }
}