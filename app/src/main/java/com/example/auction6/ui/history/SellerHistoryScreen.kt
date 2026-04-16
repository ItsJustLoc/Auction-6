package com.example.auction6.ui.history

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.auction6.data.local.OrderEntity

// Stateless UI for Seller History
@Composable
fun SellerHistoryScreen(
    items: List<Pair<OrderEntity, String>>, // order + listing title
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Button(
                onClick = onBack,
                modifier = Modifier.align(Alignment.CenterStart)
            ) { Text("Back") }

            Text(
                text = "My Sales",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        if (items.isEmpty()) {
            Text(
                text = "No sales yet.",
                modifier = Modifier.padding(16.dp),
                color = Color.Gray
            )
        } else {
            LazyColumn(contentPadding = PaddingValues(16.dp)) {
                items(items) { (order, title) ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(title, fontWeight = FontWeight.SemiBold)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Sold for: $${"%.2f".format(order.finalPrice)}")
                            Text(
                                text = "Payment: ${order.paymentStatus}",
                                color = paymentColor(order.paymentStatus)
                            )
                            Text(
                                text = "Shipping: ${shippingLabel(order.shippingStatus)}",
                                color = shippingColor(order.shippingStatus)
                            )
                            if (order.orderType == OrderEntity.ORDER_TYPE_BUY_NOW) {
                                Text("via Buy Now", color = Color(0xFF1565C0))
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun paymentColor(status: String) = when (status) {
    OrderEntity.PAYMENT_AUTHORIZED -> Color(0xFF2E7D32)
    OrderEntity.PAYMENT_FAILED,
    OrderEntity.PAYMENT_CANCELLED  -> Color(0xFFB71C1C)
    else                           -> Color(0xFF6D4C41)
}

private fun shippingColor(status: String) = when (status) {
    OrderEntity.SHIP_DELIVERED  -> Color(0xFF2E7D32)
    OrderEntity.SHIP_IN_TRANSIT -> Color(0xFF1565C0)
    OrderEntity.SHIP_RETURNED   -> Color(0xFF6D4C41)
    else                        -> Color.Gray
}

private fun shippingLabel(status: String) = when (status) {
    OrderEntity.SHIP_NOT_SHIPPED   -> "Not Shipped"
    OrderEntity.SHIP_LABEL_CREATED -> "Label Created"
    OrderEntity.SHIP_IN_TRANSIT    -> "In Transit"
    OrderEntity.SHIP_DELIVERED     -> "Delivered"
    OrderEntity.SHIP_RETURNED      -> "Returned"
    else                           -> status
}