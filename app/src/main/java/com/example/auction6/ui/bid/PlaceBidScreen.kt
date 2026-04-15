package com.example.auction6.ui.bid

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
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
    isOwnListing: Boolean,
    onPlaceBidClick: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .statusBarsPadding()
            .padding(16.dp)
    ) {
        // Top bar: Back on left
        Box(modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = onBack,
                modifier = Modifier.align(Alignment.CenterStart)
            ) { Text("Back") }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(listingTitle, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))

        if (isOwnListing) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "You cannot bid on your own listing.",
                color = Color.Red,
                fontWeight = FontWeight.SemiBold
            )
        } else {
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
        }
    }
}