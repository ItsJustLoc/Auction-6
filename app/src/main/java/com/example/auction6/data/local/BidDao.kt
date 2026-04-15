package com.example.auction6.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
//Data Access Object (DAO) for BidEntity
@Dao
interface BidDao {
    @Insert
    suspend fun insertBid(bid: BidEntity)

    // Get all bids for a listing, newest first
    @Query("SELECT * FROM bids WHERE listingId = :listingId ORDER BY timestamp DESC")
    suspend fun getBidsForListing(listingId: Int): List<BidEntity>

    // Get the highest bid for a listing (needed for 5% rule in milestone 10)
    @Query("SELECT * FROM bids WHERE listingId = :listingId ORDER BY amount DESC LIMIT 1")
    suspend fun getHighestBid(listingId: Int): BidEntity?
}