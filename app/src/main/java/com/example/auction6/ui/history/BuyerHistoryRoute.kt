package com.example.auction6.ui.history

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.auction6.data.local.DatabaseProvider
import com.example.auction6.data.local.OrderEntity

// State owner for Buyer History
@Composable
fun BuyerHistoryRoute(
    currentUserId: Long = 0L,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val db = remember(context) { DatabaseProvider.get(context) }

    var items by remember { mutableStateOf<List<Pair<OrderEntity, String>>>(emptyList()) }

    LaunchedEffect(currentUserId) {
        val orders = db.orderDao().getOrdersByBuyer(currentUserId.toInt())
        items = orders.map { order ->
            val title = db.listingDao().getListingById(order.listingId)?.title ?: "Unknown"
            order to title
        }
    }

    BuyerHistoryScreen(
        items = items,
        onBack = onBack,
        modifier = modifier
    )
}
