package com.example.auction6.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bids")
data class BidEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val listingId: Int,   // which auction item this bid belongs to
    val bidderId: Int,    // who placed the bid
    val amount: Double,   // bid amount
    val timestamp: Long   // when the bid was placed (epoch ms)
)