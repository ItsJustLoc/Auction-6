package com.example.auction6.ui.bid

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.auction6.data.local.BidEntity
import com.example.auction6.data.local.DatabaseProvider
import com.example.auction6.data.local.ListingEntity
import kotlinx.coroutines.launch

// State owner for Place Bid — enforces 5% bid increment rule
@Composable
fun PlaceBidRoute(
    listingId: Int,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val db = remember(context) { DatabaseProvider.get(context) }
    val scope = rememberCoroutineScope()

    var listing by remember { mutableStateOf<ListingEntity?>(null) }
    var currentHighestBid by remember { mutableStateOf(0.0) }
    var bidInput by remember { mutableStateOf("") }
    var resultMessage by remember { mutableStateOf("") }
    var isSuccess by remember { mutableStateOf(false) }
    var bidHistory by remember { mutableStateOf<List<BidEntity>>(emptyList()) }

    // Load listing, current highest bid, and bid history
    LaunchedEffect(listingId) {
        listing = db.listingDao().getListingById(listingId)
        val highestBid = db.bidDao().getHighestBid(listingId)
        currentHighestBid = highestBid?.amount ?: listing?.startingPrice ?: 0.0
        bidHistory = db.bidDao().getBidsForListing(listingId)
    }

    val minimumBid = currentHighestBid * 1.05

    PlaceBidScreen(
        listingTitle = listing?.title ?: "Loading...",
        currentHighestBid = currentHighestBid,
        minimumBid = minimumBid,
        bidInput = bidInput,
        onBidInputChange = { bidInput = it },
        resultMessage = resultMessage,
        isSuccess = isSuccess,
        bidHistory = bidHistory,
        onPlaceBidClick = {
            val bidNumber = bidInput.toDoubleOrNull()
            when {
                bidNumber == null -> {
                    isSuccess = false
                    resultMessage = "Please enter a valid number"
                }
                bidNumber < minimumBid -> {
                    isSuccess = false
                    resultMessage = "Invalid bid! Minimum is $${"%.2f".format(minimumBid)}"
                }
                else -> {
                    scope.launch {
                        db.bidDao().insertBid(
                            BidEntity(
                                listingId = listingId,
                                bidderId = 0, // placeholder until user sessions are added
                                amount = bidNumber,
                                timestamp = System.currentTimeMillis()
                            )
                        )
                        // Refresh state after successful bid
                        currentHighestBid = bidNumber
                        bidHistory = db.bidDao().getBidsForListing(listingId)
                        bidInput = ""
                        isSuccess = true
                        resultMessage = "Bid of $${"%.2f".format(bidNumber)} placed successfully!"
                    }
                }
            }
        },
        onBack = onBack,
        modifier = modifier
    )
}