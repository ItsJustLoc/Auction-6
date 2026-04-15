package com.example.auction6.ui.bid

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.auction6.data.local.BidEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Stateless UI for Place Bid screen
@Composable
fun PlaceBidScreen(
    listingTitle: String,
    currentHighestBid: Double,
    minimumBid: Double,
    bidInput: String,
    onBidInputChange: (String) -> Unit,
    resultMessage: String,
    isSuccess: Boolean,
    bidHistory: List<BidEntity>,
    onPlaceBidClick: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(16.dp)
    ) {
        item {
            // Top bar: Back button on left
            Box(modifier = Modifier.fillMaxWidth()) {
                Button(
                    onClick = onBack,
                    modifier = Modifier.align(Alignment.CenterStart)
                ) { Text("Back") }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(listingTitle, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            Text("Current Highest Bid: $${"%.2f".format(currentHighestBid)}")
            Text(
                text = "Minimum Bid Required: $${"%.2f".format(minimumBid)}",
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = bidInput,
                onValueChange = onBidInputChange,
                label = { Text("Enter your bid ($)") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (resultMessage.isNotEmpty()) {
                Text(
                    text = resultMessage,
                    color = if (isSuccess) Color(0xFF2E7D32) else Color.Red
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Place Bid button centered
            Box(modifier = Modifier.fillMaxWidth()) {
                Button(
                    onClick = onPlaceBidClick,
                    modifier = Modifier.align(Alignment.Center)
                ) { Text("Place Bid") }
            }

            Spacer(modifier = Modifier.height(24.dp))

            HorizontalDivider()

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Bid History",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (bidHistory.isEmpty()) {
                Text("No bids yet — be the first!", color = Color.Gray)
                Spacer(modifier = Modifier.height(8.dp))
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