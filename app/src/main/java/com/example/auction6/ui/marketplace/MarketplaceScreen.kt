package com.example.auction6.ui.marketplace

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.auction6.data.local.ListingEntity

// Stateless UI for marketplace
@Composable
fun MarketplaceScreen(
    listings: List<ListingEntity>,
    onListingClick: (listingId: Int) -> Unit,
    onLogoutClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Marketplace", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Button(onClick = onLogoutClick) { Text("Log out") }
        }

        if (listings.isEmpty()) {
            Text(
                text = "No listings yet.",
                modifier = Modifier.padding(16.dp)
            )
        } else {
            LazyColumn(contentPadding = PaddingValues(16.dp)) {
                items(listings) { listing ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                            .clickable { onListingClick(listing.id) }
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(listing.title, fontWeight = FontWeight.SemiBold)
                            Text("Starting at $${listing.startingPrice}")
                        }
                    }
                }
            }
        }
    }
}