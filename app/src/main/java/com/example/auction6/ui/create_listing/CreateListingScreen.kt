package com.example.auction6.ui.create_listing

import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.auction6.data.local.LISTING_CATEGORIES
import com.example.auction6.ui.components.AuctionTextField
import com.example.auction6.ui.components.CategoryChipGroup
import com.example.auction6.ui.theme.RetroBorder
import com.example.auction6.ui.theme.RetroCream
import com.example.auction6.ui.theme.RetroInk
import com.example.auction6.ui.theme.RetroMuted
import com.example.auction6.ui.theme.RetroOrange

@Composable
fun CreateListingScreen(
    title: String,
    onTitleChange: (String) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit,
    price: String,
    onPriceChange: (String) -> Unit,
    durationHours: String,
    onDurationChange: (String) -> Unit,
    category: String,
    onCategoryChange: (String) -> Unit,
    buyNowPrice: String,
    onBuyNowPriceChange: (String) -> Unit,
    selectedImageUri: Uri?,
    onPickImageClick: () -> Unit,
    errorMessage: String?,
    onSaveClick: () -> Unit,
    onCancelClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .statusBarsPadding()
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            "Create Listing",
            style = MaterialTheme.typography.headlineMedium,
            color = RetroInk,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        AuctionTextField(
            value = title,
            onValueChange = onTitleChange,
            label = "Title"
        )

        Spacer(modifier = Modifier.height(8.dp))

        AuctionTextField(
            value = description,
            onValueChange = onDescriptionChange,
            label = "Description",
            singleLine = false,
            minLines = 3
        )

        Spacer(modifier = Modifier.height(8.dp))

        AuctionTextField(
            value = price,
            onValueChange = onPriceChange,
            label = "Starting Price ($)",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
        )

        Spacer(modifier = Modifier.height(8.dp))

        AuctionTextField(
            value = buyNowPrice,
            onValueChange = onBuyNowPriceChange,
            label = "Buy Now Price ($) — optional",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
        )

        Spacer(modifier = Modifier.height(8.dp))

        AuctionTextField(
            value = durationHours,
            onValueChange = onDurationChange,
            label = "Duration (minutes)",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text("Category", style = MaterialTheme.typography.titleMedium, color = RetroInk)

        CategoryChipGroup(
            categories = LISTING_CATEGORIES.filter { it != "All" },
            selected = category,
            onSelect = onCategoryChange,
            modifier = Modifier.padding(horizontal = 0.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text("Photo", style = MaterialTheme.typography.titleMedium, color = RetroInk)

        Spacer(modifier = Modifier.height(8.dp))

        if (selectedImageUri != null) {
            AsyncImage(
                model = selectedImageUri,
                contentDescription = "Selected photo preview",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(160.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .clickable(onClick = onPickImageClick)
            )
        } else {
            Box(
                modifier = Modifier
                    .size(160.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(androidx.compose.ui.graphics.Color.Transparent)
                    .border(BorderStroke(1.5.dp, RetroBorder), RoundedCornerShape(8.dp))
                    .clickable(onClick = onPickImageClick),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.Outlined.CameraAlt,
                        contentDescription = "Add Photo",
                        tint = RetroMuted,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(Modifier.height(8.dp))
                    Text("Add Photo", style = MaterialTheme.typography.bodyMedium, color = RetroMuted)
                }
            }
        }

        if (errorMessage != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onSaveClick,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = RetroOrange,
                contentColor = RetroCream
            )
        ) {
            Text("Save Listing", fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(4.dp))

        TextButton(
            onClick = onCancelClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cancel", color = RetroMuted)
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
