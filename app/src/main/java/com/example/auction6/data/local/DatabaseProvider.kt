package com.example.auction6.data.local

import android.content.Context
import androidx.room.Room

object DatabaseProvider {
    @Volatile private var INSTANCE: AppDatabase? = null

    fun get(context: Context): AppDatabase {
        return INSTANCE ?: synchronized(this) {
            INSTANCE ?: Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "auction6.db"
            )
                .addMigrations(AppDatabase.MIGRATION_1_2)
                .build()
                .also { INSTANCE = it }
        }
    }
}