package com.example.auction6.ui.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Sell
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.auction6.data.local.OrderEntity
import com.example.auction6.ui.components.EmptyState
import com.example.auction6.ui.components.StatusTag
import com.example.auction6.ui.theme.RetroBorder
import com.example.auction6.ui.theme.RetroBlue
import com.example.auction6.ui.theme.RetroCard
import com.example.auction6.ui.theme.RetroCream
import com.example.auction6.ui.theme.RetroGreen
import com.example.auction6.ui.theme.RetroInk
import com.example.auction6.ui.theme.RetroAmber
import com.example.auction6.ui.theme.RetroRed
import com.example.auction6.ui.theme.RetroOrange

@Composable
fun SellerHistoryScreen(
    items: List<Pair<OrderEntity, String>>,
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
                modifier = Modifier.align(Alignment.CenterStart),
                colors = ButtonDefaults.buttonColors(containerColor = RetroOrange, contentColor = RetroCream)
            ) { Text("Back") }

            Text(
                text = "My Sales",
                style = MaterialTheme.typography.headlineMedium,
                color = RetroInk,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        if (items.isEmpty()) {
            EmptyState(
                icon = Icons.Outlined.Sell,
                title = "No sales yet",
                subtitle = "List a car part and win your first sale"
            )
        } else {
            LazyColumn(contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)) {
                items(items) { (order, title) ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(containerColor = RetroCard),
                        border = androidx.compose.foundation.BorderStroke(1.dp, RetroBorder)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                title,
                                style = MaterialTheme.typography.titleMedium,
                                color = RetroInk
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                "Sold for: $${"%.2f".format(order.finalPrice)}",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                color = RetroInk
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                StatusTag(
                                    label = "Payment: ${order.paymentStatus}",
                                    color = paymentTagColor(order.paymentStatus)
                                )
                                StatusTag(
                                    label = "Ship: ${shippingLabel(order.shippingStatus)}",
                                    color = shippingTagColor(order.shippingStatus)
                                )
                                if (order.orderType == OrderEntity.ORDER_TYPE_BUY_NOW) {
                                    StatusTag(label = "Buy Now", color = RetroBlue)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun paymentTagColor(status: String) = when (status) {
    OrderEntity.PAYMENT_AUTHORIZED -> RetroGreen
    OrderEntity.PAYMENT_FAILED,
    OrderEntity.PAYMENT_CANCELLED  -> RetroRed
    else                           -> RetroAmber
}

private fun shippingTagColor(status: String) = when (status) {
    OrderEntity.SHIP_DELIVERED  -> RetroGreen
    OrderEntity.SHIP_RETURNED   -> RetroRed
    OrderEntity.SHIP_IN_TRANSIT -> RetroBlue
    else                        -> RetroAmber
}

private fun shippingLabel(status: String) = when (status) {
    OrderEntity.SHIP_NOT_SHIPPED   -> "Not Shipped"
    OrderEntity.SHIP_LABEL_CREATED -> "Label Created"
    OrderEntity.SHIP_IN_TRANSIT    -> "In Transit"
    OrderEntity.SHIP_DELIVERED     -> "Delivered"
    OrderEntity.SHIP_RETURNED      -> "Returned"
    else                           -> status
}
