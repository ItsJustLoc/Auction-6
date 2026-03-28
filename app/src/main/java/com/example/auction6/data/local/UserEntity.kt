package com.example.auction6.data.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "users",
    indices = [Index(value = ["email"], unique = true)]
)
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val email: String,
    val passwordHash: String,   // for now you can store plain text if your team allows, but name it like this
    val isVerified: Boolean = false,
    val verificationCode: String = " "
)