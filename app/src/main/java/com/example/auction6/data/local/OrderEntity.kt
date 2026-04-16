package com.example.auction6.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val listingId: Int,
    val buyerId: Int,
    val sellerId: Int,
    val finalPrice: Double,
    val paymentStatus: String,     // PENDING | AUTHORIZED | FAILED | CANCELLED
    val shippingStatus: String,    // NOT_SHIPPED | LABEL_CREATED | IN_TRANSIT | DELIVERED | RETURNED
    val shippingAddress: String = "",
    val orderType: String = ORDER_TYPE_AUCTION  // AUCTION | BUY_NOW
) {
    companion object {
        const val PAYMENT_PENDING    = "PENDING"
        const val PAYMENT_AUTHORIZED = "AUTHORIZED"
        const val PAYMENT_FAILED     = "FAILED"
        const val PAYMENT_CANCELLED  = "CANCELLED"

        const val SHIP_NOT_SHIPPED   = "NOT_SHIPPED"
        const val SHIP_LABEL_CREATED = "LABEL_CREATED"
        const val SHIP_IN_TRANSIT    = "IN_TRANSIT"
        const val SHIP_DELIVERED     = "DELIVERED"
        const val SHIP_RETURNED      = "RETURNED"

        const val ORDER_TYPE_AUCTION  = "AUCTION"
        const val ORDER_TYPE_BUY_NOW  = "BUY_NOW"
    }
}