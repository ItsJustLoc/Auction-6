package com.example.auction6.ui.marketplace

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.auction6.data.local.BidEntity
import com.example.auction6.data.local.ListingEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Stateless UI for listing detail
@Composable
fun ListingDetailScreen(
    listing: ListingEntity?,
    bidHistory: List<BidEntity>,
    onBack: () -> Unit,
    onPlaceBidClick: () -> Unit,
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
                Text(listing.title, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text(listing.description)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Starting Price: $${listing.startingPrice}", fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(4.dp))
                val endDate = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
                    .format(Date(listing.endTime))
                Text("Ends: $endDate")
                Spacer(modifier = Modifier.height(4.dp))

                // Show current highest bid if any
                if (bidHistory.isNotEmpty()) {
                    val highest = bidHistory.maxByOrNull { it.amount }
                    Text(
                        text = "Highest Bid: $${"%.2f".format(highest?.amount)}",
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF1565C0)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Place Bid centered
                Box(modifier = Modifier.fillMaxWidth()) {
                    Button(
                        onClick = onPlaceBidClick,
                        modifier = Modifier.align(Alignment.Center)
                    ) { Text("Place Bid") }
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