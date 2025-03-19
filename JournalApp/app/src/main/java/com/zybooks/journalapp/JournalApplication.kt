package com.zybooks.journalapp

import android.app.Application
import android.os.StrictMode
import androidx.room.Room
import com.zybooks.journalapp.data.JournalDatabase

class JournalApplication : Application() {

    companion object {
        lateinit var instance: JournalApplication
            private set
    }

    // Database instance accessible throughout the app
    lateinit var database: JournalDatabase
        private set

    override fun onCreate() {
        super.onCreate()
        instance = this

        // Initialize Room database
        database = JournalDatabase.getDatabase(this)

        // Set StrictMode for development to catch bugs early
        // Using BuildConfig.DEBUG from our custom BuildConfig class
        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(
                StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build()
            )

            StrictMode.setVmPolicy(
                StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects()
                    .detectLeakedClosableObjects()
                    .penaltyLog()
                    .build()
            )
        }

        // Initialize other dependencies as needed
        setupWorkManager()
    }

    private fun setupWorkManager() {
        // You can setup WorkManager here if needed for background tasks
        // such as periodic data cleanup, backup, etc.
    }
}

