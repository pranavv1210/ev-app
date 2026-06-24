package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "profiles")
data class Profile(
    @PrimaryKey val mobile: String,
    val name: String,
    val email: String,
    val role: String, // "CUSTOMER", "DRIVER", "ADMIN"
    val isFemale: Boolean = false,
    val carbonSavedTotal: Double = 0.0, // in kg
    val referCode: String = "",
    val isLoggedIn: Boolean = false
)

@Entity(tableName = "rides")
data class Ride(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val customerMobile: String,
    val customerName: String,
    val pickupName: String,
    val pickupLat: Double,
    val pickupLng: Double,
    val destinationName: String,
    val destLat: Double,
    val destLng: Double,
    val vehicleType: String, // "EV Bike", "EV Auto", "EV Cab"
    val fare: Double,
    val status: String, // "REQUESTED", "ACCEPTED", "ARRIVED", "STARTED", "COMPLETED", "CANCELLED"
    val timestamp: Long = System.currentTimeMillis(),
    val scheduledTime: Long? = null, // null for immediate
    val driverMobile: String? = null,
    val driverName: String? = null,
    val driverVehicleNo: String? = null,
    val driverLat: Double? = null,
    val driverLng: Double? = null,
    val ratingCustomer: Float? = null,
    val ratingDriver: Float? = null,
    val co2SavedKg: Double = 0.0,
    val isWomenMode: Boolean = false
)

@Entity(tableName = "driver_docs")
data class DriverDoc(
    @PrimaryKey val driverMobile: String,
    val driverName: String,
    val vehicleType: String, // "EV Bike", "EV Auto", "EV Cab"
    val vehicleNo: String,
    val dlNo: String,
    val certificateNo: String, // Insurance
    val status: String = "PENDING", // "PENDING", "APPROVED", "REJECTED"
    val isFemale: Boolean = false
)

@Entity(tableName = "emergency_contacts")
data class EmergencyContact(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val ownerMobile: String,
    val contactName: String,
    val contactPhone: String
)
