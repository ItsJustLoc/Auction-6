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
    currentUserId: Long = 0L,
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

    // Load listing and current highest bid
    LaunchedEffect(listingId) {
        listing = db.listingDao().getListingById(listingId)
        val highestBid = db.bidDao().getHighestBid(listingId)
        currentHighestBid = highestBid?.amount ?: listing?.startingPrice ?: 0.0
    }

    val minimumBid = currentHighestBid * 1.05
    val isOwnListing = listing != null && listing!!.sellerId == currentUserId.toInt()

    PlaceBidScreen(
        listingTitle = listing?.title ?: "Loading...",
        currentHighestBid = currentHighestBid,
        minimumBid = minimumBid,
        bidInput = bidInput,
        onBidInputChange = { bidInput = it },
        resultMessage = resultMessage,
        isSuccess = isSuccess,
        isOwnListing = isOwnListing,
        onPlaceBidClick = {
            val bidNumber = bidInput.toDoubleOrNull()
            when {
                isOwnListing -> {
                    isSuccess = false
                    resultMessage = "You cannot bid on your own listing"
                }
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
                        // Re-check listing state at write time — it may have closed via
                        // Buy Now or auction expiry while the user was on this screen
                        val fresh = db.listingDao().getListingById(listingId)
                        val auctionAlreadyClosed = fresh == null ||
                            fresh.endTime < System.currentTimeMillis()
                        val orderAlreadyExists = db.orderDao()
                            .getOrderByListingId(listingId) != null

                        when {
                            auctionAlreadyClosed || orderAlreadyExists -> {
                                isSuccess = false
                                resultMessage = "This auction is no longer active — bids cannot be placed."
                            }
                            else -> {
                                db.bidDao().insertBid(
                                    BidEntity(
                                        listingId = listingId,
                                        bidderId = currentUserId.toInt(),
                                        amount = bidNumber,
                                        timestamp = System.currentTimeMillis()
                                    )
                                )
                                currentHighestBid = bidNumber
                                bidInput = ""
                                isSuccess = true
                                resultMessage = "Bid of $${"%.2f".format(bidNumber)} placed successfully!"
                            }
                        }
                    }
                }
            }
        },
        onBack = onBack,
        modifier = modifier
    )
}