package com.example.auction6.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

// Data Access Object (DAO) for OrderEntity
@Dao
interface OrderDao {
    @Insert
    suspend fun insertOrder(order: OrderEntity)

    // One listing can only ever produce one order
    @Query("SELECT * FROM orders WHERE listingId = :listingId LIMIT 1")
    suspend fun getOrderByListingId(listingId: Int): OrderEntity?

    // Buyer history (buyerId = 0 placeholder for now)
    @Query("SELECT * FROM orders WHERE buyerId = :buyerId")
    suspend fun getOrdersByBuyer(buyerId: Int): List<OrderEntity>

    // Seller history (sellerId = 0 placeholder for now)
    @Query("SELECT * FROM orders WHERE sellerId = :sellerId")
    suspend fun getOrdersBySeller(sellerId: Int): List<OrderEntity>

    // Shipping state transitions (milestone 14)
    @Query("UPDATE orders SET status = :status WHERE id = :orderId")
    suspend fun updateOrderStatus(orderId: Int, status: String)
}
