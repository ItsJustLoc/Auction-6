package com.example.auction6.ui.marketplace

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch
import com.example.auction6.data.local.BidEntity
import com.example.auction6.data.local.DatabaseProvider
import com.example.auction6.data.local.ListingEntity
import com.example.auction6.data.local.OrderEntity

// State Owner for Listing Detail
@Composable
fun ListingDetailRoute(
    listingId: Int,
    currentUserId: Long = 0L,
    refreshTrigger: Int = 0,
    onBack: () -> Unit,
    onPlaceBidClick: () -> Unit,
    onBuyNowClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val db = remember(context) { DatabaseProvider.get(context) }
    val scope = rememberCoroutineScope()

    var listing by remember { mutableStateOf<ListingEntity?>(null) }
    var bidHistory by remember { mutableStateOf<List<BidEntity>>(emptyList()) }
    var auctionEnded by remember { mutableStateOf(false) }
    var highestBid by remember { mutableStateOf<BidEntity?>(null) }
    var order by remember { mutableStateOf<OrderEntity?>(null) }

    LaunchedEffect(listingId, refreshTrigger) {
        val loaded = db.listingDao().getListingById(listingId)
        val bids = db.bidDao().getBidsForListing(listingId)
        val topBid = db.bidDao().getHighestBid(listingId)
        val ended = loaded != null && loaded.endTime < System.currentTimeMillis()
        val existingOrder = db.orderDao().getOrderByListingId(listingId)

        // Auto-create order if auction closed by time with a winning bid (and no order yet)
        if (ended && topBid != null && loaded != null && existingOrder == null) {  // loaded non-null implied by ended check
            db.orderDao().insertOrder(
                OrderEntity(
                    listingId = listingId,
                    buyerId = topBid.bidderId,
                    sellerId = loaded.sellerId,
                    finalPrice = topBid.amount,
                    paymentStatus = OrderEntity.PAYMENT_AUTHORIZED,
                    shippingStatus = OrderEntity.SHIP_NOT_SHIPPED,
                    orderType = OrderEntity.ORDER_TYPE_AUCTION
                )
            )
            order = db.orderDao().getOrderByListingId(listingId)
        } else {
            order = existingOrder
        }

        listing = loaded
        bidHistory = bids
        highestBid = topBid
        auctionEnded = ended
    }

    val isSeller = listing != null && listing!!.sellerId == currentUserId.toInt()
    val isBuyer  = order != null && order!!.buyerId == currentUserId.toInt()

    ListingDetailScreen(
        listing = listing,
        bidHistory = bidHistory,
        auctionEnded = auctionEnded,
        highestBid = highestBid,
        order = order,
        isSeller = isSeller,
        isBuyer = isBuyer,
        onBack = onBack,
        onPlaceBidClick = onPlaceBidClick,
        onBuyNowClick = onBuyNowClick,
        onDeleteClick = {
            scope.launch {
                db.listingDao().deleteListingById(listingId)
                onBack()
            }
        },
        onUpdateShipping = { orderId, newShippingStatus ->
            scope.launch {
                db.orderDao().updateShippingStatus(orderId, newShippingStatus)
                order = db.orderDao().getOrderByListingId(listingId)
            }
        },
        onRelistClick = {
            scope.launch {
                // Remove failed order and all bids, then reopen the listing for 30 minutes
                db.orderDao().deleteOrderByListingId(listingId)
                db.bidDao().deleteAllBidsForListing(listingId)
                val newEndTime = System.currentTimeMillis() + 30 * 60_000L
                db.listingDao().updateEndTime(listingId, newEndTime)
                // Refresh local state
                val refreshed = db.listingDao().getListingById(listingId)
                listing = refreshed
                bidHistory = emptyList()
                highestBid = null
                auctionEnded = false
                order = null
            }
        },
        modifier = modifier
    )
}