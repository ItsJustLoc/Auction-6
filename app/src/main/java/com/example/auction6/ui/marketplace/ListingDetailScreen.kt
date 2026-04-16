package com.example.auction6.ui.marketplace

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.auction6.data.local.BidEntity
import com.example.auction6.data.local.ListingEntity
import com.example.auction6.data.local.OrderEntity
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Stateless UI for listing detail
@Composable
fun ListingDetailScreen(
    listing: ListingEntity?,
    bidHistory: List<BidEntity>,
    auctionEnded: Boolean,
    highestBid: BidEntity?,
    order: OrderEntity?,
    isSeller: Boolean,
    isBuyer: Boolean,
    onBack: () -> Unit,
    onPlaceBidClick: () -> Unit,
    onBuyNowClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onUpdateShipping: (orderId: Int, newShippingStatus: String) -> Unit,
    onRelistClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(16.dp)
    ) {
        item {
            // Back on left
            Box(modifier = Modifier.fillMaxWidth()) {
                Button(
                    onClick = onBack,
                    modifier = Modifier.align(Alignment.CenterStart)
                ) { Text("Back") }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (listing == null) {
                Text("Loading...")
            } else {
                Text(
                    listing.title,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF1A1612),
                    letterSpacing = (-0.5).sp
                )
                Spacer(modifier = Modifier.height(6.dp))

                // Listing photo (if one was uploaded)
                if (listing.imagePath.isNotBlank()) {
                    AsyncImage(
                        model = File(listing.imagePath),
                        contentDescription = "Listing photo",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                Text(
                    listing.description,
                    fontSize = 15.sp,
                    color = Color(0xFF6B6560),
                    lineHeight = 22.sp
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text("Starting Price: $${listing.startingPrice}", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(4.dp))
                val endDate = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
                    .format(Date(listing.endTime))
                Text(
                    "Ends: $endDate",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFFB87333)
                )

                // Buy Now price badge (only when active)
                if (!auctionEnded && listing.buyNowPrice > 0.0) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Buy Now: $${"%.2f".format(listing.buyNowPrice)}",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1565C0),
                        fontSize = 16.sp
                    )
                }

                if (bidHistory.isNotEmpty() && !auctionEnded) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Highest Bid: $${"%.2f".format(highestBid?.amount)}",
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF1565C0)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                if (auctionEnded) {
                    // ── CLOSED LISTING ──────────────────────────────────────────
                    if (order != null) {
                        // Header depends on how the order was created
                        if (order.orderType == OrderEntity.ORDER_TYPE_BUY_NOW) {
                            Text(
                                text = "Purchased via Buy Now",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1565C0)
                            )
                        } else {
                            Text(
                                text = "Auction Ended",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2E7D32)
                            )
                            if (highestBid != null) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Winning Bid: $${"%.2f".format(highestBid.amount)}",
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // ── Order Details Card (TC6) ───────────────────────────
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text("Order Details", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Spacer(modifier = Modifier.height(6.dp))
                                Text("Buyer: ${if (isBuyer) "You" else "—"}", fontSize = 14.sp)
                                Text("Final Price: $${"%.2f".format(order.finalPrice)}", fontSize = 14.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Payment: ${order.paymentStatus}",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = paymentColor(order.paymentStatus)
                                )
                                Text(
                                    text = "Shipping: ${shippingLabel(order.shippingStatus)}",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = shippingColor(order.shippingStatus)
                                )
                                if (order.shippingAddress.isNotBlank()) {
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text("Ship to: ${order.shippingAddress}", fontSize = 14.sp, color = Color.Gray)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // ── Action buttons based on payment + shipping state ──
                        when {
                            order.paymentStatus == OrderEntity.PAYMENT_FAILED ||
                            order.paymentStatus == OrderEntity.PAYMENT_CANCELLED -> {
                                if (isBuyer) {
                                    Text(
                                        text = "Your payment did not go through. The seller may relist this item.",
                                        color = Color(0xFFB71C1C)
                                    )
                                }
                                if (isSeller) {
                                    Text(
                                        text = "Payment was not received. You can relist this item.",
                                        color = Color(0xFFB71C1C)
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Button(
                                        onClick = onRelistClick,
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color(0xFF1565C0)
                                        )
                                    ) { Text("Relist Item") }
                                }
                            }

                            order.paymentStatus == OrderEntity.PAYMENT_PENDING -> {
                                Text("Payment pending...", color = Color(0xFF6D4C41))
                            }

                            order.paymentStatus == OrderEntity.PAYMENT_AUTHORIZED -> {
                                when (order.shippingStatus) {
                                    OrderEntity.SHIP_NOT_SHIPPED -> {
                                        if (isSeller) {
                                            Text(
                                                text = "Payment received — please ship the item.",
                                                color = Color(0xFF2E7D32),
                                                fontWeight = FontWeight.SemiBold
                                            )
                                            Spacer(modifier = Modifier.height(8.dp))
                                            Box(modifier = Modifier.fillMaxWidth()) {
                                                Button(
                                                    onClick = { onUpdateShipping(order.id, OrderEntity.SHIP_LABEL_CREATED) },
                                                    modifier = Modifier.align(Alignment.Center)
                                                ) { Text("Create Shipping Label") }
                                            }
                                        } else if (isBuyer) {
                                            Text("Waiting for seller to ship...", color = Color.Gray)
                                        }
                                    }
                                    OrderEntity.SHIP_LABEL_CREATED -> {
                                        if (isSeller) {
                                            Box(modifier = Modifier.fillMaxWidth()) {
                                                Button(
                                                    onClick = { onUpdateShipping(order.id, OrderEntity.SHIP_IN_TRANSIT) },
                                                    modifier = Modifier.align(Alignment.Center)
                                                ) { Text("Mark In Transit") }
                                            }
                                        } else if (isBuyer) {
                                            Text("Label created — item will ship soon.", color = Color.Gray)
                                        }
                                    }
                                    OrderEntity.SHIP_IN_TRANSIT -> {
                                        if (isSeller) {
                                            Box(modifier = Modifier.fillMaxWidth()) {
                                                Button(
                                                    onClick = { onUpdateShipping(order.id, OrderEntity.SHIP_DELIVERED) },
                                                    modifier = Modifier.align(Alignment.Center)
                                                ) { Text("Mark Delivered") }
                                            }
                                        }
                                        if (isBuyer) {
                                            Box(modifier = Modifier.fillMaxWidth()) {
                                                Button(
                                                    onClick = { onUpdateShipping(order.id, OrderEntity.SHIP_DELIVERED) },
                                                    modifier = Modifier.align(Alignment.Center)
                                                ) { Text("Confirm Delivery") }
                                            }
                                        }
                                    }
                                    OrderEntity.SHIP_DELIVERED -> {
                                        Text(
                                            text = "Delivered",
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF2E7D32)
                                        )
                                        if (isSeller || isBuyer) {
                                            Spacer(modifier = Modifier.height(8.dp))
                                            OutlinedButton(
                                                onClick = { onUpdateShipping(order.id, OrderEntity.SHIP_RETURNED) },
                                                colors = ButtonDefaults.outlinedButtonColors(
                                                    contentColor = Color(0xFF6D4C41)
                                                )
                                            ) { Text("Mark as Returned") }
                                        }
                                    }
                                    OrderEntity.SHIP_RETURNED -> {
                                        Text(
                                            text = "Returned",
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF6D4C41)
                                        )
                                    }
                                }
                            }
                        }

                    } else {
                        // Ended with no bids and no buy now
                        Text(
                            text = "Auction Ended — No Bids",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Gray
                        )
                        if (isSeller) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = onRelistClick,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF1565C0)
                                )
                            ) { Text("Relist Item") }
                        }
                    }

                } else {
                    // ── ACTIVE LISTING ──────────────────────────────────────────
                    if (isSeller) {
                        Text(
                            text = "Your Listing",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        if (bidHistory.isEmpty()) {
                            Button(
                                onClick = onDeleteClick,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFB71C1C)
                                )
                            ) { Text("Delete Listing") }
                        } else {
                            Text(
                                text = "Cannot delete — bids have been placed",
                                color = Color.Gray
                            )
                        }
                    } else {
                        // Buyer: Place Bid + optional Buy Now side by side
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Button(onClick = onPlaceBidClick) { Text("Place Bid") }
                            if (listing.buyNowPrice > 0.0) {
                                Spacer(modifier = Modifier.width(12.dp))
                                Button(
                                    onClick = onBuyNowClick,
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFF1565C0)
                                    )
                                ) { Text("Buy Now") }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(8.dp))
                Text("Bid History", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))

                if (bidHistory.isEmpty()) {
                    Text("No bids yet — be the first!", color = Color.Gray)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }

        items(bidHistory) { bid ->
            val formattedTime = SimpleDateFormat("MMM dd, HH:mm:ss", Locale.getDefault())
                .format(Date(bid.timestamp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "$${"%.2f".format(bid.amount)}",
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(text = formattedTime, color = Color.Gray)
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
    OrderEntity.SHIP_DELIVERED     -> Color(0xFF2E7D32)
    OrderEntity.SHIP_IN_TRANSIT    -> Color(0xFF1565C0)
    OrderEntity.SHIP_RETURNED      -> Color(0xFF6D4C41)
    else                           -> Color.Gray
}

private fun shippingLabel(status: String) = when (status) {
    OrderEntity.SHIP_NOT_SHIPPED   -> "Not Shipped"
    OrderEntity.SHIP_LABEL_CREATED -> "Label Created"
    OrderEntity.SHIP_IN_TRANSIT    -> "In Transit"
    OrderEntity.SHIP_DELIVERED     -> "Delivered"
    OrderEntity.SHIP_RETURNED      -> "Returned"
    else                           -> status
}