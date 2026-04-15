package com.example.auction6.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val listingId: Int,
    val buyerId: Int,       // winning bidder (0 = placeholder until user sessions)
    val sellerId: Int,      // listing seller (0 = placeholder until user sessions)
    val finalPrice: Double,
    val status: String      // see companion object for valid values
) {
    companion object {
        const val STATUS_PURCHASED = "PURCHASED"
        const val STATUS_SHIPPED   = "SHIPPED"
        const val STATUS_DELIVERED = "DELIVERED"
    }
}
