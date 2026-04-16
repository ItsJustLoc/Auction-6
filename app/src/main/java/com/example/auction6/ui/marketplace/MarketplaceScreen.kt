package com.example.auction6.ui.marketplace

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Storefront
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.auction6.data.local.LISTING_CATEGORIES
import com.example.auction6.data.local.ListingEntity
import com.example.auction6.ui.components.CategoryChipGroup
import com.example.auction6.ui.components.EmptyState
import com.example.auction6.ui.components.ListingCard
import com.example.auction6.ui.theme.RetroBorder
import com.example.auction6.ui.theme.RetroBlue
import com.example.auction6.ui.theme.RetroCream
import com.example.auction6.ui.theme.RetroInk
import com.example.auction6.ui.theme.RetroMuted
import com.example.auction6.ui.theme.RetroOrange

@OptIn(ExperimentalMaterial3Api::class)
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
    Scaffold(
        modifier = modifier.statusBarsPadding(),
        containerColor = RetroCream,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "AUCTION6",
                        style = MaterialTheme.typography.displayLarge,
                        color = RetroInk,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                actions = {
                    IconButton(onClick = onLogoutClick) {
                        Icon(Icons.Default.Person, contentDescription = "Log out", tint = RetroMuted)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = RetroCream)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onCreateListingClick,
                containerColor = RetroOrange
            ) {
                Icon(Icons.Default.Add, contentDescription = "List Item", tint = Color.White)
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(bottom = 88.dp)
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onBuyerHistoryClick,
                        modifier = Modifier.weight(1f),
                        border = BorderStroke(1.dp, RetroBlue),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = RetroBlue)
                    ) { Text("Purchases") }

                    OutlinedButton(
                        onClick = onSellerHistoryClick,
                        modifier = Modifier.weight(1f),
                        border = BorderStroke(1.dp, RetroBlue),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = RetroBlue)
                    ) { Text("Sales") }
                }
            }

            item {
                CategoryChipGroup(
                    categories = LISTING_CATEGORIES,
                    selected = selectedCategory,
                    onSelect = onCategorySelected
                )
            }

            if (listings.isEmpty()) {
                item {
                    Spacer(Modifier.height(48.dp))
                    EmptyState(
                        icon = Icons.Outlined.Storefront,
                        title = "No listings here",
                        subtitle = "Be the first to list a car part — tap + to get started"
                    )
                }
            } else {
                items(listings, key = { it.id }) { listing ->
                    ListingCard(
                        listing = listing,
                        isYours = listing.sellerId == currentUserId.toInt(),
                        onClick = { onListingClick(listing.id) },
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}
