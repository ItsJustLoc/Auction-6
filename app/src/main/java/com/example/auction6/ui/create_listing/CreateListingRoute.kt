package com.example.auction6.ui.create_listing

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.auction6.data.local.DatabaseProvider
import com.example.auction6.data.local.LISTING_CATEGORIES
import com.example.auction6.data.local.ListingEntity
import kotlinx.coroutines.launch

// State owner for Create Listing
@Composable
fun CreateListingRoute(
    currentUserId: Long = 0L,
    onSaved: () -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val listingDao = remember(context) { DatabaseProvider.get(context).listingDao() }
    val scope = rememberCoroutineScope()

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var durationHours by remember { mutableStateOf("") }
    var category by remember { mutableStateOf(LISTING_CATEGORIES.last()) } // defaults to "Other"
    var errorMessage by remember { mutableStateOf<String?>(null) }

    CreateListingScreen(
        title = title,
        onTitleChange = { title = it },
        description = description,
        onDescriptionChange = { description = it },
        price = price,
        onPriceChange = { price = it },
        durationHours = durationHours,
        onDurationChange = { durationHours = it },
        category = category,
        onCategoryChange = { category = it },
        errorMessage = errorMessage,
        onSaveClick = {
            val parsedPrice = price.toDoubleOrNull()
            val parsedHours = durationHours.toLongOrNull()

            when {
                title.isBlank() -> errorMessage = "Title is required"
                description.isBlank() -> errorMessage = "Description is required"
                parsedPrice == null || parsedPrice <= 0 -> errorMessage = "Enter a valid price"
                parsedHours == null || parsedHours <= 0 -> errorMessage = "Enter a valid duration"
                else -> {
                    errorMessage = null
                    val endTime = System.currentTimeMillis() + parsedHours * 60_000L
                    scope.launch {
                        listingDao.insertListing(
                            ListingEntity(
                                title = title.trim(),
                                description = description.trim(),
                                startingPrice = parsedPrice,
                                endTime = endTime,
                                sellerId = currentUserId.toInt(),
                                category = category
                            )
                        )
                        onSaved()
                    }
                }
            }
        },
        onCancelClick = onCancel,
        modifier = modifier
    )
}