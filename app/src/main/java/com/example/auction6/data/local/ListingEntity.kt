package com.example.auction6.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "listings")
data class ListingEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String,
    val startingPrice: Double,
    val endTime: Long,       // stored as epoch milliseconds
    val sellerId: Int,       // references users.id
    val category: String = "Other",
    val buyNowPrice: Double = 0.0   // 0.0 means no Buy Now option
)