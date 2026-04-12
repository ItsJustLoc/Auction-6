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

    @Query("SELECT * FROM listings WHERE id = :listingId")
    suspend fun getListingById(listingId: Int): ListingEntity?
}