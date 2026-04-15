package com.example.auction6.ui.marketplace

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.auction6.data.local.ListingEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Stateless UI for listing detail
@Composable
fun ListingDetailScreen(
    listing: ListingEntity?,
    onBack: () -> Unit,
    onPlaceBidClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.statusBarsPadding().padding(16.dp)) {
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

            Spacer(modifier = Modifier.height(24.dp))

            // Place Bid centered
            Box(modifier = Modifier.fillMaxWidth()) {
                Button(
                    onClick = onPlaceBidClick,
                    modifier = Modifier.align(Alignment.Center)
                ) { Text("Place Bid") }
            }
        }
    }
}