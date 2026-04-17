package com.example.auction6.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import coil.compose.AsyncImage
import com.example.auction6.data.local.ListingEntity
import kotlinx.coroutines.delay
import com.example.auction6.ui.theme.RetroBorder
import com.example.auction6.ui.theme.RetroBlue
import com.example.auction6.ui.theme.RetroAmber
import com.example.auction6.ui.theme.RetroCard
import com.example.auction6.ui.theme.RetroCream
import com.example.auction6.ui.theme.RetroGreen
import com.example.auction6.ui.theme.RetroInk
import com.example.auction6.ui.theme.RetroMuted
import com.example.auction6.ui.theme.RetroOrange
import java.io.File

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CategoryChipGroup(
    categories: List<String>,
    selected: String,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    FlowRow(
        modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        categories.forEach { cat ->
            FilterChip(
                selected = cat == selected,
                onClick = { onSelect(cat) },
                label = { Text(cat, fontSize = 13.sp, fontWeight = FontWeight.Medium) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = RetroAmber,
                    selectedLabelColor = RetroInk,
                    containerColor = RetroCard,
                    labelColor = RetroMuted
                ),
                border = FilterChipDefaults.filterChipBorder(
                    enabled = true,
                    selected = cat == selected,
                    borderColor = RetroBorder,
                    selectedBorderColor = RetroAmber
                ),
                shape = RoundedCornerShape(20.dp)
            )
        }
    }
}

@Composable
fun AuctionTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    singleLine: Boolean = true,
    minLines: Int = 1,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: androidx.compose.ui.text.input.VisualTransformation =
        androidx.compose.ui.text.input.VisualTransformation.None
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = singleLine,
        minLines = minLines,
        keyboardOptions = keyboardOptions,
        visualTransformation = visualTransformation,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = RetroOrange,
            unfocusedBorderColor = RetroBorder,
            focusedLabelColor = RetroOrange,
            unfocusedContainerColor = RetroCard,
            focusedContainerColor = RetroCard
        )
    )
}

@Composable
fun ListingCard(
    listing: ListingEntity,
    isYours: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var timeRemainingMs by remember { mutableStateOf(listing.endTime - System.currentTimeMillis()) }
    LaunchedEffect(listing.endTime) {
        while (timeRemainingMs > 0L) {
            delay(1_000L)
            timeRemainingMs = listing.endTime - System.currentTimeMillis()
        }
    }
    val timeLabel = when {
        timeRemainingMs <= 0 -> "Ended"
        timeRemainingMs < 60_000L -> "${timeRemainingMs / 1_000}s"
        timeRemainingMs < 3_600_000L -> "${timeRemainingMs / 60_000}m left"
        else -> "${timeRemainingMs / 3_600_000}h left"
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = RetroCard),
        border = BorderStroke(2.dp, RetroBorder)
    ) {
        Column {
            if (listing.imagePath.isNotBlank()) {
                AsyncImage(
                    model = File(listing.imagePath),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp))
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp))
                        .background(RetroBorder)
                )
            }

            Column(modifier = Modifier.padding(12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        listing.title,
                        style = MaterialTheme.typography.titleMedium,
                        color = RetroInk,
                        modifier = Modifier.weight(1f)
                    )
                    if (isYours) {
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = RetroAmber.copy(alpha = 0.15f),
                            border = BorderStroke(1.dp, RetroAmber)
                        ) {
                            Text(
                                "YOUR LISTING",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = RetroAmber,
                                    fontSize = 9.sp,
                                    letterSpacing = 1.sp
                                ),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                Text(
                    listing.category,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = RetroBlue
                )

                Spacer(Modifier.height(6.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "$${listing.startingPrice}",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            color = RetroAmber,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    )
                    Text(
                        " · starting bid",
                        fontSize = 12.sp,
                        color = RetroMuted
                    )
                    Spacer(Modifier.weight(1f))
                    Text(
                        timeLabel,
                        fontSize = 12.sp,
                        color = RetroMuted,
                        fontFamily = FontFamily.Monospace
                    )
                }

                if (listing.buyNowPrice > 0.0) {
                    Text(
                        "Buy Now: $${listing.buyNowPrice}",
                        style = MaterialTheme.typography.labelSmall.copy(color = RetroGreen)
                    )
                }
            }
        }
    }
}

@Composable
fun StatusTag(label: String, color: Color) {
    Surface(
        shape = RoundedCornerShape(4.dp),
        color = color.copy(alpha = 0.12f),
        border = BorderStroke(1.dp, color.copy(alpha = 0.5f))
    ) {
        Text(
            label,
            style = MaterialTheme.typography.labelSmall.copy(
                color = color,
                fontWeight = FontWeight.SemiBold
            ),
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@Composable
fun EmptyState(
    icon: ImageVector,
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .padding(40.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            icon,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = RetroBorder
        )
        Spacer(Modifier.height(16.dp))
        Text(title, style = MaterialTheme.typography.titleMedium, color = RetroInk)
        Spacer(Modifier.height(8.dp))
        Text(
            subtitle,
            style = MaterialTheme.typography.bodyMedium.copy(color = RetroMuted),
            textAlign = TextAlign.Center
        )
    }
}
