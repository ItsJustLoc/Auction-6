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
import com.example.auction6.data.local.ListingEntity

// State Owner for Listing Detail
@Composable
fun ListingDetailRoute(
    listingId: Int,
    onBack: () -> Unit,
    onPlaceBidClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val listingDao = remember(context) { DatabaseProvider.get(context).listingDao() }

    var listing by remember { mutableStateOf<ListingEntity?>(null) }

    LaunchedEffect(listingId) {
        listing = listingDao.getListingById(listingId)
    }

    ListingDetailScreen(
        listing = listing,
        onBack = onBack,
        onPlaceBidClick = onPlaceBidClick,
        modifier = modifier
    )
}