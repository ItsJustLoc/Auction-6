package com.example.auction6.ui.buy_now

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.auction6.data.local.DatabaseProvider
import com.example.auction6.data.local.ListingEntity
import com.example.auction6.data.local.OrderEntity
import kotlinx.coroutines.launch

// State owner for the Buy Now flow
@Composable
fun BuyNowRoute(
    listingId: Int,
    currentUserId: Long = 0L,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val db = remember(context) { DatabaseProvider.get(context) }
    val scope = rememberCoroutineScope()

    var listing by remember { mutableStateOf<ListingEntity?>(null) }
    var shippingAddress by remember { mutableStateOf("") }
    var resultMessage by remember { mutableStateOf<String?>(null) }
    var resultSuccess by remember { mutableStateOf(false) }
    var addressError by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(listingId) {
        listing = db.listingDao().getListingById(listingId)
    }

    BuyNowScreen(
        listing = listing,
        shippingAddress = shippingAddress,
        onAddressChange = {
            shippingAddress = it
            addressError = null
        },
        addressError = addressError,
        resultMessage = resultMessage,
        resultSuccess = resultSuccess,
        onAuthorizePayment = {
            if (shippingAddress.isBlank()) {
                addressError = "Shipping address is required"
                return@BuyNowScreen
            }
            val l = listing ?: return@BuyNowScreen
            scope.launch {
                // Guard: reject if listing has already closed or an order already exists
                val fresh = db.listingDao().getListingById(listingId)
                if (fresh == null || fresh.endTime < System.currentTimeMillis() ||
                    db.orderDao().getOrderByListingId(listingId) != null) {
                    resultSuccess = false
                    resultMessage = "This listing is no longer available for Buy Now."
                    return@launch
                }
                // Close the listing immediately (Buy Now wins)
                db.listingDao().updateEndTime(listingId, System.currentTimeMillis() - 1)
                // Create order with AUTHORIZED payment
                db.orderDao().insertOrder(
                    OrderEntity(
                        listingId = listingId,
                        buyerId = currentUserId.toInt(),
                        sellerId = l.sellerId,
                        finalPrice = l.buyNowPrice,
                        paymentStatus = OrderEntity.PAYMENT_AUTHORIZED,
                        shippingStatus = OrderEntity.SHIP_NOT_SHIPPED,
                        shippingAddress = shippingAddress.trim(),
                        orderType = OrderEntity.ORDER_TYPE_BUY_NOW
                    )
                )
                resultSuccess = true
                resultMessage = "Payment authorized! The seller will be notified to ship your item."
            }
        },
        onSimulateFailure = {
            if (shippingAddress.isBlank()) {
                addressError = "Shipping address is required"
                return@BuyNowScreen
            }
            val l = listing ?: return@BuyNowScreen
            scope.launch {
                // Guard: same check as authorize
                val fresh = db.listingDao().getListingById(listingId)
                if (fresh == null || fresh.endTime < System.currentTimeMillis() ||
                    db.orderDao().getOrderByListingId(listingId) != null) {
                    resultSuccess = false
                    resultMessage = "This listing is no longer available for Buy Now."
                    return@launch
                }
                // Close the listing
                db.listingDao().updateEndTime(listingId, System.currentTimeMillis() - 1)
                // Create order with FAILED payment
                db.orderDao().insertOrder(
                    OrderEntity(
                        listingId = listingId,
                        buyerId = currentUserId.toInt(),
                        sellerId = l.sellerId,
                        finalPrice = l.buyNowPrice,
                        paymentStatus = OrderEntity.PAYMENT_FAILED,
                        shippingStatus = OrderEntity.SHIP_NOT_SHIPPED,
                        shippingAddress = shippingAddress.trim(),
                        orderType = OrderEntity.ORDER_TYPE_BUY_NOW
                    )
                )
                resultSuccess = false
                resultMessage = "Payment failed. The seller will be notified and may relist the item."
            }
        },
        onBackToListing = onBack,
        modifier = modifier
    )
}