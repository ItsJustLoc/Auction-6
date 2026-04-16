package com.example.auction6.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface OrderDao {
    @Insert
    suspend fun insertOrder(order: OrderEntity)

    // One listing can only ever produce one order
    @Query("SELECT * FROM orders WHERE listingId = :listingId LIMIT 1")
    suspend fun getOrderByListingId(listingId: Int): OrderEntity?

    @Query("SELECT * FROM orders WHERE buyerId = :buyerId")
    suspend fun getOrdersByBuyer(buyerId: Int): List<OrderEntity>

    @Query("SELECT * FROM orders WHERE sellerId = :sellerId")
    suspend fun getOrdersBySeller(sellerId: Int): List<OrderEntity>

    @Query("UPDATE orders SET paymentStatus = :paymentStatus WHERE id = :orderId")
    suspend fun updatePaymentStatus(orderId: Int, paymentStatus: String)

    @Query("UPDATE orders SET shippingStatus = :shippingStatus WHERE id = :orderId")
    suspend fun updateShippingStatus(orderId: Int, shippingStatus: String)

    @Query("UPDATE orders SET shippingAddress = :address WHERE id = :orderId")
    suspend fun updateShippingAddress(orderId: Int, address: String)

    @Query("DELETE FROM orders WHERE listingId = :listingId")
    suspend fun deleteOrderByListingId(listingId: Int)
}