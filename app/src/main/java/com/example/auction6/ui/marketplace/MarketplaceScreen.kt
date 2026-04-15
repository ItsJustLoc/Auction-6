package com.example.auction6.ui.marketplace

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.auction6.data.local.LISTING_CATEGORIES
import com.example.auction6.data.local.ListingEntity

// Stateless UI for marketplace
@Composable
fun MarketplaceScreen(
    listings: List<ListingEntity>,
    currentUserId: Long = 0L,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    onListingClick: (listingId: Int) -> Unit,
    onLogoutClick: () -> Unit,
    onCreateListingClick: () -> Unit,
    onBuyerHistoryClick: () -> Unit,
    onSellerHistoryClick: () -> Unit,
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

        // History row: Purchases left, Sales right
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp)
        ) {
            OutlinedButton(
                onClick = onBuyerHistoryClick,
                modifier = Modifier.align(Alignment.CenterStart)
            ) { Text("Purchases") }

            OutlinedButton(
                onClick = onSellerHistoryClick,
                modifier = Modifier.align(Alignment.CenterEnd)
            ) { Text("Sales") }
        }

        // Category filter chips
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(LISTING_CATEGORIES) { cat ->
                if (cat == selectedCategory) {
                    Button(onClick = { onCategorySelected(cat) }) { Text(cat) }
                } else {
                    OutlinedButton(onClick = { onCategorySelected(cat) }) { Text(cat) }
                }
            }
        }

        // Content area fills remaining vertical space to eliminate bottom whitespace
        Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
            if (listings.isEmpty()) {
                Text(
                    text = "No listings yet.",
                    modifier = Modifier.padding(16.dp)
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    items(listings) { listing ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp)
                                .clickable { onListingClick(listing.id) }
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(listing.title, fontWeight = FontWeight.SemiBold)
                                    if (listing.sellerId == currentUserId.toInt()) {
                                        Text(
                                            text = "Your listing",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.tertiary
                                        )
                                    }
                                }
                                Text(
                                    text = listing.category,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = "Starting at $${listing.startingPrice}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}