package com.example.auction6.ui.navigation

sealed class Route(val route: String) {
    object Login : Route("login")
    object Marketplace : Route("marketplace")
}