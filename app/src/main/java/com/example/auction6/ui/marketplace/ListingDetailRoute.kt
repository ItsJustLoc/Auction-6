package com.example.auction6.ui.marketplace

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.auction6.data.local.BidEntity
import com.example.auction6.data.local.DatabaseProvider
import com.example.auction6.data.local.ListingEntity

// State Owner for Listing Detail
@Composable
fun ListingDetailRoute(
    listingId: Int,
    refreshTrigger: Int = 0,
    onBack: () -> Unit,
    onPlaceBidClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val db = remember(context) { DatabaseProvider.get(context) }

    var listing by remember { mutableStateOf<ListingEntity?>(null) }
    var bidHistory by remember { mutableStateOf<List<BidEntity>>(emptyList()) }
    var auctionEnded by remember { mutableStateOf(false) }
    var highestBid by remember { mutableStateOf<BidEntity?>(null) }

    LaunchedEffect(listingId, refreshTrigger) {
        val loaded = db.listingDao().getListingById(listingId)
        val bids = db.bidDao().getBidsForListing(listingId)
        val topBid = db.bidDao().getHighestBid(listingId)
        listing = loaded
        bidHistory = bids
        highestBid = topBid
        auctionEnded = loaded != null && loaded.endTime < System.currentTimeMillis()
    }

    ListingDetailScreen(
        listing = listing,
        bidHistory = bidHistory,
        auctionEnded = auctionEnded,
        highestBid = highestBid,
        onBack = onBack,
        onPlaceBidClick = onPlaceBidClick,
        modifier = modifier
    )
}