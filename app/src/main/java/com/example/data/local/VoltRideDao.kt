package com.example.data.local

import androidx.room.*
import com.example.data.model.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ProfileDao {
    @Query("SELECT * FROM profiles WHERE mobile = :mobile LIMIT 1")
    suspend fun getProfileByMobile(mobile: String): Profile?

    @Query("SELECT * FROM profiles WHERE isLoggedIn = 1 LIMIT 1")
    fun getActiveProfile(): Flow<Profile?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: Profile)

    @Query("UPDATE profiles SET isLoggedIn = 0")
    suspend fun logoutAll()

    @Query("UPDATE profiles SET carbonSavedTotal = carbonSavedTotal + :added WHERE mobile = :mobile")
    suspend fun addCarbonSaving(mobile: String, added: Double)
}

@Dao
interface RideDao {
    @Query("SELECT * FROM rides ORDER BY timestamp DESC")
    fun getAllRides(): Flow<List<Ride>>

    @Query("SELECT * FROM rides WHERE customerMobile = :mobile ORDER BY timestamp DESC")
    fun getRidesForCustomer(mobile: String): Flow<List<Ride>>

    @Query("SELECT * FROM rides WHERE driverMobile = :mobile ORDER BY timestamp DESC")
    fun getRidesForDriver(mobile: String): Flow<List<Ride>>

    @Query("SELECT * FROM rides WHERE id = :id LIMIT 1")
    fun getRideById(id: Long): Flow<Ride?>

    @Query("SELECT * FROM rides WHERE id = :id LIMIT 1")
    suspend fun getRideByIdSync(id: Long): Ride?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRide(ride: Ride): Long

    @Query("UPDATE rides SET status = :status WHERE id = :id")
    suspend fun updateRideStatus(id: Long, status: String)

    @Query("UPDATE rides SET driverLat = :lat, driverLng = :lng WHERE id = :id")
    suspend fun updateDriverLocation(id: Long, lat: Double, lng: Double)

    @Query("UPDATE rides SET status = :status, driverMobile = :driverMobile, driverName = :driverName, driverVehicleNo = :driverVehicleNo WHERE id = :id")
    suspend fun acceptRide(id: Long, status: String, driverMobile: String, driverName: String, driverVehicleNo: String)
}

@Dao
interface DriverDocDao {
    @Query("SELECT * FROM driver_docs ORDER BY driverMobile DESC")
    fun getAllDocs(): Flow<List<DriverDoc>>

    @Query("SELECT * FROM driver_docs WHERE driverMobile = :mobile LIMIT 1")
    suspend fun getDocByMobile(mobile: String): DriverDoc?

    @Query("SELECT * FROM driver_docs WHERE driverMobile = :mobile")
    fun getDocByMobileFlow(mobile: String): Flow<DriverDoc?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDoc(doc: DriverDoc)

    @Query("UPDATE driver_docs SET status = :status WHERE driverMobile = :mobile")
    suspend fun updateDocStatus(mobile: String, status: String)
}

@Dao
interface EmergencyContactDao {
    @Query("SELECT * FROM emergency_contacts WHERE ownerMobile = :mobile")
    fun getContactsForOwner(mobile: String): Flow<List<EmergencyContact>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContact(contact: EmergencyContact)

    @Delete
    suspend fun deleteContact(contact: EmergencyContact)
}
