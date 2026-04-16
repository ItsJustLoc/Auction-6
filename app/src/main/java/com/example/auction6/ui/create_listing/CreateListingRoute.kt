package com.example.auction6.ui.create_listing

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import java.io.File

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
    var category by remember { mutableStateOf(LISTING_CATEGORIES.last()) }
    var buyNowPrice by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Standard Android system image picker — no permission required
    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
    }

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
        buyNowPrice = buyNowPrice,
        onBuyNowPriceChange = { buyNowPrice = it },
        selectedImageUri = selectedImageUri,
        onPickImageClick = { imagePicker.launch("image/*") },
        errorMessage = errorMessage,
        onSaveClick = {
            val parsedPrice = price.toDoubleOrNull()
            val parsedHours = durationHours.toLongOrNull()
            val parsedBuyNow = if (buyNowPrice.isBlank()) 0.0 else buyNowPrice.toDoubleOrNull()

            when {
                title.isBlank() -> errorMessage = "Title is required"
                description.isBlank() -> errorMessage = "Description is required"
                parsedPrice == null || parsedPrice <= 0 -> errorMessage = "Enter a valid price"
                parsedHours == null || parsedHours <= 0 -> errorMessage = "Enter a valid duration"
                parsedBuyNow == null || parsedBuyNow < 0 -> errorMessage = "Buy Now price must be a positive number (or leave blank)"
                parsedBuyNow > 0 && parsedBuyNow <= parsedPrice -> errorMessage = "Buy Now price must be higher than the starting price"
                else -> {
                    errorMessage = null
                    val endTime = System.currentTimeMillis() + parsedHours * 60_000L
                    scope.launch {
                        // Copy image to internal storage so it persists independently of the source URI
                        val imagePath = selectedImageUri
                            ?.let { copyImageToInternalStorage(context, it) }
                            ?: ""
                        listingDao.insertListing(
                            ListingEntity(
                                title = title.trim(),
                                description = description.trim(),
                                startingPrice = parsedPrice,
                                endTime = endTime,
                                sellerId = currentUserId.toInt(),
                                category = category,
                                buyNowPrice = parsedBuyNow ?: 0.0,
                                imagePath = imagePath
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

/** Copies a content URI into the app's private listing_images directory and returns the file path. */
private fun copyImageToInternalStorage(context: Context, sourceUri: Uri): String {
    val dir = File(context.filesDir, "listing_images").also { it.mkdirs() }
    val dest = File(dir, "${System.currentTimeMillis()}.jpg")
    context.contentResolver.openInputStream(sourceUri)?.use { input ->
        dest.outputStream().use { input.copyTo(it) }
    }
    return dest.absolutePath
}