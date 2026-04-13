package com.example.auction6.ui.marketplace

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.auction6.data.local.ListingEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.example.auction6.ui.bid.BidSection

// Stateless UI for listing detail
@Composable
fun ListingDetailScreen(
    listing: ListingEntity?,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(16.dp)) {
        Button(onClick = onBack) { Text("Back") }

        Spacer(modifier = Modifier.height(16.dp))

        if (listing == null) {
            Text("Loading...")
        } else {
            val safeListing = listing
            Text(listing.title, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text(safeListing.description)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Starting Price: $${safeListing.startingPrice}", fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(4.dp))
            val endDate = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
                .format(Date(safeListing.endTime))
            Text("Ends: $endDate")


            //Bid Section
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Place a Bid",
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(8.dp))
            BidSection(currentHighestBid = safeListing.startingPrice)
        }


    }
}
