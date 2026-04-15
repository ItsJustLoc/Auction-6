package com.example.auction6.ui.marketplace

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.auction6.data.local.DatabaseProvider
import com.example.auction6.data.local.LISTING_CATEGORIES
import com.example.auction6.data.local.ListingEntity

// State Owner for Marketplace
@Composable
fun MarketplaceRoute(
    modifier: Modifier = Modifier,
    refreshTrigger: Int = 0,
    onLogoutSuccess: () -> Unit,
    onListingClick: (listingId: Int) -> Unit,
    onCreateListingClick: () -> Unit
) {
    val context = LocalContext.current
    val listingDao = remember(context) { DatabaseProvider.get(context).listingDao() }

    var listings by remember { mutableStateOf<List<ListingEntity>>(emptyList()) }
    var selectedCategory by remember { mutableStateOf("All") }

    LaunchedEffect(refreshTrigger, selectedCategory) {
        listings = if (selectedCategory == "All") {
            listingDao.getAllListings()
        } else {
            listingDao.getListingsByCategory(selectedCategory)
        }
    }

    MarketplaceScreen(
        listings = listings,
        selectedCategory = selectedCategory,
        onCategorySelected = { selectedCategory = it },
        onListingClick = onListingClick,
        onLogoutClick = { onLogoutSuccess() },
        onCreateListingClick = onCreateListingClick,
        modifier = modifier
    )
}