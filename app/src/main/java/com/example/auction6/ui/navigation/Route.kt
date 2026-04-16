package com.example.auction6.ui.navigation

sealed class Route(val route: String) {
    object Login : Route("login")
    object Register: Route("register")
    object Marketplace : Route("marketplace")
    object Verify : Route("verify/{userId}"){
        fun createRoute(userId: Long) = "verify/$userId"
    }
    object ListingDetail : Route("listing/{listingId}") {
        fun createRoute(listingId: Int) = "listing/$listingId"
    }
    object CreateListing : Route("create_listing")
    object PlaceBid : Route("place_bid/{listingId}") {
        fun createRoute(listingId: Int) = "place_bid/$listingId"
    }
    object BuyerHistory : Route("buyer_history")
    object SellerHistory : Route("seller_history")
    object BuyNow : Route("buy_now/{listingId}") {
        fun createRoute(listingId: Int) = "buy_now/$listingId"
    }
}