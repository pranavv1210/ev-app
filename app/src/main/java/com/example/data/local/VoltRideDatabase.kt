package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.*

@Database(entities = [Profile::class, Ride::class, DriverDoc::class, EmergencyContact::class], version = 1, exportSchema = false)
abstract class VoltRideDatabase : RoomDatabase() {
    abstract fun profileDao(): ProfileDao
    abstract fun rideDao(): RideDao
    abstract fun driverDocDao(): DriverDocDao
    abstract fun emergencyContactDao(): EmergencyContactDao

    companion object {
        @Volatile
        private var INSTANCE: VoltRideDatabase? = null

        fun getDatabase(context: Context): VoltRideDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    VoltRideDatabase::class.java,
                    "voltride_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
