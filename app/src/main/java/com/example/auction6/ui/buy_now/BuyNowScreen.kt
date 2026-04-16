package com.example.auction6.ui.buy_now

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.auction6.data.local.ListingEntity

// Stateless UI for Buy Now checkout
@Composable
fun BuyNowScreen(
    listing: ListingEntity?,
    shippingAddress: String,
    onAddressChange: (String) -> Unit,
    addressError: String?,
    resultMessage: String?,
    resultSuccess: Boolean,
    onAuthorizePayment: () -> Unit,
    onSimulateFailure: () -> Unit,
    onBackToListing: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(16.dp)
    ) {
        // Back button — top left
        Box(modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = onBackToListing,
                modifier = Modifier.align(Alignment.CenterStart)
            ) { Text("Back") }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (listing == null) {
            Text("Loading...")
            return@Column
        }

        Text("Buy Now", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Text(listing.title, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Buy Now Price: $${"%.2f".format(listing.buyNowPrice)}",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1565C0)
        )

        Spacer(modifier = Modifier.height(24.dp))
        HorizontalDivider()
        Spacer(modifier = Modifier.height(16.dp))

        if (resultMessage == null) {
            // Checkout form
            Text("Shipping Address", fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = shippingAddress,
                onValueChange = onAddressChange,
                label = { Text("Full shipping address") },
                minLines = 2,
                isError = addressError != null,
                modifier = Modifier.fillMaxWidth()
            )
            if (addressError != null) {
                Text(
                    text = addressError,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Primary: Authorize Payment (centered)
            Box(modifier = Modifier.fillMaxWidth()) {
                Button(
                    onClick = onAuthorizePayment,
                    modifier = Modifier.align(Alignment.Center)
                ) { Text("Authorize Payment") }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Secondary: Simulate failure (left-aligned, outlined)
            OutlinedButton(
                onClick = onSimulateFailure,
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color(0xFFB71C1C)
                )
            ) { Text("Simulate Payment Failure") }

        } else {
            // Result panel
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = if (resultSuccess) Color(0xFFE8F5E9) else Color(0xFFFFEBEE)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = if (resultSuccess) "Payment Authorized" else "Payment Failed",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (resultSuccess) Color(0xFF2E7D32) else Color(0xFFB71C1C)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(resultMessage)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Box(modifier = Modifier.fillMaxWidth()) {
                Button(
                    onClick = onBackToListing,
                    modifier = Modifier.align(Alignment.Center)
                ) { Text("Back to Listing") }
            }
        }
    }
}