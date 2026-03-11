package com.example.auction6

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import com.example.auction6.data.local.DatabaseProvider
import com.example.auction6.data.local.UserEntity
import com.example.auction6.ui.navigation.AppNavHost
import com.example.auction6.ui.theme.Auction6Theme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Temporary Milestone 4 verification code.
        // Confirms Room can insert and query a sample user
        // and that the data persists after app restart.

        Log.d("ROOM_TEST", "onCreate started")

        // Get the singleton Room database instance and the DAO we need.
        val db = DatabaseProvider.get(applicationContext)
        val userDao = db.userDao()

        // Run database work in a coroutine because Room suspend functions
        // cannot be called directly on the main thread.
        lifecycleScope.launch {
            try {
                Log.d("ROOM_TEST", "Coroutine launched")

                // Fixed sample user used only for Room verification.
                // Re-launching the app should find the same user again,
                // which helps prove the data persists locally.
                val email = "test@auction6.com"

                // Check whether the sample user already exists.
                val existingUser = userDao.findByEmail(email)
                Log.d("ROOM_TEST", "Existing user query result = $existingUser")

                // Insert the sample user only if it is not already in the DB.
                if (existingUser == null) {
                    val newId = userDao.insert(
                        UserEntity(
                            email = email,
                            passwordHash = "123456",
                            isVerified = false
                        )
                    )
                    Log.d("ROOM_TEST", "Inserted user with id = $newId")
                } else {
                    Log.d("ROOM_TEST", "User already exists: $existingUser")
                }

                // Query the user again after insert / existence check
                // to confirm the row can be read successfully.
                val loadedUser = userDao.findByEmail(email)
                Log.d("ROOM_TEST", "Loaded user after query = $loadedUser")

                // Count total users as one more proof that Room is working.
                val count = userDao.countUsers()
                Log.d("ROOM_TEST", "Total users in DB = $count")

            } catch (e: Exception) {
                // If anything fails, print the full stack trace in Logcat.
                Log.e("ROOM_TEST", "Database test failed", e)
            }
        }

        // Normal app UI startup.
        setContent {
            Auction6Theme {
                AppNavHost()
            }
        }
    }
}