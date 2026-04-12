package com.example.auction6.ui.marketplace

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.auction6.data.local.ListingEntity
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.ui.text.style.TextAlign

// Stateless UI for marketplace
@Composable
fun MarketplaceScreen(
    listings: List<ListingEntity>,
    onListingClick: (listingId: Int) -> Unit,
    onLogoutClick: () -> Unit,
    onCreateListingClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.statusBarsPadding()) {
        // Title row
        Text(
            text = "Marketplace",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, start = 16.dp, end = 16.dp)
        )

        // Button bar: logout left, + List Item centered
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Button(
                onClick = onLogoutClick,
                modifier = Modifier.align(Alignment.CenterStart)
            ) { Text("Log out") }

            Button(
                onClick = onCreateListingClick,
                modifier = Modifier.align(Alignment.CenterEnd)
            ) { Text("+ List Item") }
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