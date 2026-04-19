package com.example.auction6.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ListingDao {
    @Insert
    suspend fun insertListing(listing: ListingEntity)

    @Query("SELECT * FROM listings")
    suspend fun getAllListings(): List<ListingEntity>

    @Query("SELECT * FROM listings WHERE endTime > :now")
    suspend fun getActiveListings(now: Long): List<ListingEntity>

    @Query("SELECT * FROM listings WHERE id = :listingId")
    suspend fun getListingById(listingId: Int): ListingEntity?

    @Query("SELECT * FROM listings WHERE category = :category")
    suspend fun getListingsByCategory(category: String): List<ListingEntity>

    @Query("SELECT * FROM listings WHERE category = :category AND endTime > :now")
    suspend fun getActiveListingsByCategory(category: String, now: Long): List<ListingEntity>

    @Query("DELETE FROM listings WHERE id = :listingId")
    suspend fun deleteListingById(listingId: Int)

    @Query("UPDATE listings SET endTime = :newEndTime WHERE id = :listingId")
    suspend fun updateEndTime(listingId: Int, newEndTime: Long)
}