package com.example.data.repository

import android.content.Context
import com.example.data.local.*
import com.example.data.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class VoltRideRepository(private val db: VoltRideDatabase) {

    private val profileDao = db.profileDao()
    private val rideDao = db.rideDao()
    private val docDao = db.driverDocDao()
    private val emergencyDao = db.emergencyContactDao()

    // Active Profile reactive stream
    val activeProfile: Flow<Profile?> = profileDao.getActiveProfile()

    // Real-time system coordination (Simulated live server channels)
    private val _onlineDrivers = MutableStateFlow<List<SimulatedDriver>>(emptyList())
    val onlineDrivers = _onlineDrivers.asStateFlow()

    private val _liveRideUpdates = MutableStateFlow<Ride?>(null)
    val liveRideUpdates = _liveRideUpdates.asStateFlow()

    init {
        // Seed initial mock drivers moving on the map near key areas
        _onlineDrivers.value = listOf(
            SimulatedDriver("drv_1", "Ananya Hegde", "EV Bike", "KA-03-EM-1123", 12.9716, 77.5946, 4.8f, true),
            SimulatedDriver("drv_2", "Rohan Sharma", "EV Cab", "KA-51-EE-4552", 12.9352, 77.6244, 4.9f, false),
            SimulatedDriver("drv_3", "Priya Nair", "EV Auto", "KA-04-EA-9912", 12.9784, 77.6408, 4.7f, true),
            SimulatedDriver("drv_4", "Karthik Gowda", "EV Cab", "KA-01-EE-8800", 12.9279, 77.6271, 4.6f, false),
            SimulatedDriver("drv_5", "Shalini Rao", "EV Bike", "KA-02-EM-5541", 12.9612, 77.6010, 4.9f, true)
        )
    }

    suspend fun getProfileByMobile(mobile: String): Profile? {
        return profileDao.getProfileByMobile(mobile)
    }

    suspend fun createOrLoginProfile(
        mobile: String,
        name: String,
        email: String,
        role: String,
        isFemale: Boolean,
        referCode: String = ""
    ) {
        profileDao.logoutAll()
        val existing = profileDao.getProfileByMobile(mobile)
        val profile = existing?.copy(isLoggedIn = true, role = role) 
            ?: Profile(
                mobile = mobile,
                name = name,
                email = email,
                role = role,
                isFemale = isFemale,
                carbonSavedTotal = 0.0,
                referCode = if (referCode.isNotEmpty()) referCode else "VR-${(1000..9999).random()}",
                isLoggedIn = true
            )
        profileDao.insertProfile(profile)
    }

    suspend fun logout() {
        profileDao.logoutAll()
        _liveRideUpdates.value = null
    }

    suspend fun addCarbon(mobile: String, co2: Double) {
        profileDao.addCarbonSaving(mobile, co2)
    }

    // Driver Onboarding Documents
    fun getAllDocs(): Flow<List<DriverDoc>> = docDao.getAllDocs()
    
    suspend fun getDoc(mobile: String): DriverDoc? = docDao.getDocByMobile(mobile)

    fun getDocFlow(mobile: String): Flow<DriverDoc?> = docDao.getDocByMobileFlow(mobile)

    suspend fun submitDoc(doc: DriverDoc) {
        docDao.insertDoc(doc)
    }

    suspend fun updateDocStatus(mobile: String, status: String) {
        docDao.updateDocStatus(mobile, status)
    }

    // Rides Control
    fun getAllRides(): Flow<List<Ride>> = rideDao.getAllRides()

    fun getRidesForCustomer(mobile: String): Flow<List<Ride>> = rideDao.getRidesForCustomer(mobile)

    fun getRidesForDriver(mobile: String): Flow<List<Ride>> = rideDao.getRidesForDriver(mobile)

    fun getRideById(id: Long): Flow<Ride?> = rideDao.getRideById(id)

    suspend fun getRideByIdSync(id: Long): Ride? = rideDao.getRideByIdSync(id)

    suspend fun bookRide(
        customerMobile: String,
        customerName: String,
        pickup: String,
        pickupLat: Double,
        pickupLng: Double,
        dest: String,
        destLat: Double,
        destLng: Double,
        vehicleType: String,
        fare: Double,
        isWomenMode: Boolean
    ): Long {
        // Calculate realistic carbon savings
        // EV Bike saving is ~120g per km compared to petrol
        // EV Auto is ~180g, EV Cab is ~250g
        val distanceSim = 4.5 // average km
        val mult = when (vehicleType) {
            "EV Bike" -> 0.12
            "EV Auto" -> 0.18
            else -> 0.25
        }
        val co2 = distanceSim * mult

        val ride = Ride(
            customerMobile = customerMobile,
            customerName = customerName,
            pickupName = pickup,
            pickupLat = pickupLat,
            pickupLng = pickupLng,
            destinationName = dest,
            destLat = destLat,
            destLng = destLng,
            vehicleType = vehicleType,
            fare = fare,
            status = "REQUESTED",
            co2SavedKg = co2,
            isWomenMode = isWomenMode
        )
        val id = rideDao.insertRide(ride)
        _liveRideUpdates.value = ride.copy(id = id)
        return id
    }

    suspend fun acceptRideByDriver(rideId: Long, driverMobile: String, driverName: String, vehicleNo: String) {
        rideDao.acceptRide(rideId, "ACCEPTED", driverMobile, driverName, vehicleNo)
        val updated = rideDao.getRideByIdSync(rideId)
        _liveRideUpdates.value = updated
    }

    suspend fun updateRideStatus(rideId: Long, status: String) {
        rideDao.updateRideStatus(rideId, status)
        val updated = rideDao.getRideByIdSync(rideId)
        if (updated != null) {
            _liveRideUpdates.value = updated
            // Add Carbon savings to user once completed
            if (status == "COMPLETED") {
                addCarbon(updated.customerMobile, updated.co2SavedKg)
            }
        }
    }

    fun publishExternalUpdate(ride: Ride) {
        _liveRideUpdates.value = ride
    }

    // Emergency Contacts
    fun getEmergencyContacts(ownerMobile: String): Flow<List<EmergencyContact>> {
        return emergencyDao.getContactsForOwner(ownerMobile)
    }

    suspend fun addEmergencyContact(contact: EmergencyContact) {
        emergencyDao.insertContact(contact)
    }

    suspend fun updateDriverLocation(id: Long, lat: Double, lng: Double) {
        rideDao.updateDriverLocation(id, lat, lng)
        val updated = rideDao.getRideByIdSync(id)
        if (updated != null) {
            _liveRideUpdates.value = updated
        }
    }

    suspend fun deleteEmergencyContact(contact: EmergencyContact) {
        emergencyDao.deleteContact(contact)
    }

    companion object {
        @Volatile
        private var INSTANCE: VoltRideRepository? = null

        fun getRepository(context: Context): VoltRideRepository {
            return INSTANCE ?: synchronized(this) {
                val db = VoltRideDatabase.getDatabase(context)
                val repo = VoltRideRepository(db)
                INSTANCE = repo
                repo
            }
        }
    }
}

// Support class for simulated live active vehicles on the maps
data class SimulatedDriver(
    val id: String,
    val name: String,
    val vehicleType: String,
    val vehicleNo: String,
    var lat: Double,
    var lng: Double,
    val rating: Float,
    val isFemale: Boolean
)
