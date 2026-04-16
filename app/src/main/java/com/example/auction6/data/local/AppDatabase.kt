package com.example.auction6.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [UserEntity::class, ListingEntity::class, BidEntity::class, OrderEntity::class],
    version = 7,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun listingDao(): ListingDao
    abstract fun bidDao(): BidDao
    abstract fun orderDao(): OrderDao

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "ALTER TABLE users ADD COLUMN verificationCode TEXT NOT NULL DEFAULT ''"
                )
            }
        }

        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS listings (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        title TEXT NOT NULL,
                        description TEXT NOT NULL,
                        startingPrice REAL NOT NULL,
                        endTime INTEGER NOT NULL,
                        sellerId INTEGER NOT NULL
                    )
                    """.trimIndent()
                )
            }
        }

        val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS bids (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        listingId INTEGER NOT NULL,
                        bidderId INTEGER NOT NULL,
                        amount REAL NOT NULL,
                        timestamp INTEGER NOT NULL
                    )
                    """.trimIndent()
                )
            }
        }

        val MIGRATION_4_5 = object : Migration(4, 5) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS orders (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        listingId INTEGER NOT NULL,
                        buyerId INTEGER NOT NULL,
                        sellerId INTEGER NOT NULL,
                        finalPrice REAL NOT NULL,
                        status TEXT NOT NULL
                    )
                    """.trimIndent()
                )
            }
        }

        val MIGRATION_5_6 = object : Migration(5, 6) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "ALTER TABLE listings ADD COLUMN category TEXT NOT NULL DEFAULT 'Other'"
                )
            }
        }

        val MIGRATION_6_7 = object : Migration(6, 7) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // Recreate orders table with separate paymentStatus / shippingStatus / address / orderType
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS orders_new (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        listingId INTEGER NOT NULL,
                        buyerId INTEGER NOT NULL,
                        sellerId INTEGER NOT NULL,
                        finalPrice REAL NOT NULL,
                        paymentStatus TEXT NOT NULL,
                        shippingStatus TEXT NOT NULL,
                        shippingAddress TEXT NOT NULL DEFAULT '',
                        orderType TEXT NOT NULL DEFAULT 'AUCTION'
                    )
                    """.trimIndent()
                )
                // Migrate existing rows — old status maps to shipping states
                db.execSQL(
                    """
                    INSERT INTO orders_new (id, listingId, buyerId, sellerId, finalPrice,
                        paymentStatus, shippingStatus, shippingAddress, orderType)
                    SELECT id, listingId, buyerId, sellerId, finalPrice,
                        'AUTHORIZED',
                        CASE status
                            WHEN 'SHIPPED'    THEN 'IN_TRANSIT'
                            WHEN 'DELIVERED'  THEN 'DELIVERED'
                            ELSE                   'NOT_SHIPPED'
                        END,
                        '',
                        'AUCTION'
                    FROM orders
                    """.trimIndent()
                )
                db.execSQL("DROP TABLE orders")
                db.execSQL("ALTER TABLE orders_new RENAME TO orders")

                // Add Buy Now price column to listings
                db.execSQL(
                    "ALTER TABLE listings ADD COLUMN buyNowPrice REAL NOT NULL DEFAULT 0.0"
                )
            }
        }
    }
}