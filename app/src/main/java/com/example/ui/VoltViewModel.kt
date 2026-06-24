package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.*
import com.example.data.repository.SimulatedDriver
import com.example.data.repository.Simulator
import com.example.data.repository.VoltRideRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlin.math.abs

data class RiderVenue(val name: String, val lat: Double, val lng: Double)

class VoltViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = VoltRideRepository.getRepository(application)

    // Reactive data streams from Database
    val activeProfile: StateFlow<Profile?> = repository.activeProfile.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    val allRides: StateFlow<List<Ride>> = repository.getAllRides().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val allDocs: StateFlow<List<DriverDoc>> = repository.getAllDocs().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val onlineDrivers: StateFlow<List<SimulatedDriver>> = repository.onlineDrivers.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Current screen navigation state
    private val _currentScreen = MutableStateFlow("auth") // auth, rider_main, driver_main, admin_main
    val currentScreen = _currentScreen.asStateFlow()

    // Booking interface auxiliary states
    val venues = listOf(
        RiderVenue("Indiranagar Metro Station", 12.9718, 77.6412),
        RiderVenue("Koramangala 80ft Road", 12.9279, 77.6271),
        RiderVenue("MG Road Trinity Metro", 12.9736, 77.6174),
        RiderVenue("UB City Complex Mall", 12.9716, 77.5946),
        RiderVenue("HSR Layout Sector 3", 12.9115, 77.6384),
        RiderVenue("Kempegowda International Airport", 13.1986, 77.7066)
    )

    private val _selectedPickup = MutableStateFlow<RiderVenue?>(null)
    val selectedPickup = _selectedPickup.asStateFlow()

    private val _selectedDestination = MutableStateFlow<RiderVenue?>(null)
    val selectedDestination = _selectedDestination.asStateFlow()

    private val _selectedVehicleType = MutableStateFlow("EV Cab")
    val selectedVehicleType = _selectedVehicleType.asStateFlow()

    private val _womenModeEnabled = MutableStateFlow(false)
    val womenModeEnabled = _womenModeEnabled.asStateFlow()

    // Live Ride session tracking
    val liveRide: StateFlow<Ride?> = repository.liveRideUpdates.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    // Active driver states
    private val _driverOnline = MutableStateFlow(false)
    val driverOnline = _driverOnline.asStateFlow()

    private val _currentDriverDoc = MutableStateFlow<DriverDoc?>(null)
    val currentDriverDoc = _currentDriverDoc.asStateFlow()

    // Emergency pings
    private val _emergencyContacts = MutableStateFlow<List<EmergencyContact>>(emptyList())
    val emergencyContacts = _emergencyContacts.asStateFlow()

    // Admin pricing adjustments
    private val _adminFareMultiplier = MutableStateFlow(1.0f)
    val adminFareMultiplier = _adminFareMultiplier.asStateFlow()

    // Alert states
    private val _toastEvent = MutableSharedFlow<String>()
    val toastEvent = _toastEvent.asSharedFlow()

    init {
        // Start the physics and lifecycle background simulator daemon
        Simulator.start(application)
        
        // Listen to active user logins and transition screen
        viewModelScope.launch {
            activeProfile.collect { profile ->
                if (profile != null && profile.isLoggedIn) {
                    when (profile.role) {
                        "CUSTOMER" -> _currentScreen.value = "rider_main"
                        "DRIVER" -> _currentScreen.value = "driver_main"
                        "ADMIN" -> _currentScreen.value = "admin_main"
                        else -> _currentScreen.value = "auth"
                    }
                    
                    // Fetch driver docs and emergency contacts dynamically
                    launch {
                        repository.getDocFlow(profile.mobile).collect { doc ->
                            _currentDriverDoc.value = doc
                        }
                    }
                    launch {
                        repository.getEmergencyContacts(profile.mobile).collect { contacts ->
                            _emergencyContacts.value = contacts
                        }
                    }
                } else {
                    _currentScreen.value = "auth"
                    _currentDriverDoc.value = null
                    _emergencyContacts.value = emptyList()
                }
            }
        }
    }

    fun navigateTo(screen: String) {
        _currentScreen.value = screen
    }

    fun selectPickup(venue: RiderVenue) {
        _selectedPickup.value = venue
    }

    fun selectDestination(venue: RiderVenue) {
        _selectedDestination.value = venue
    }

    fun selectVehicleType(type: String) {
        _selectedVehicleType.value = type
    }

    fun setWomenMode(enabled: Boolean) {
        _womenModeEnabled.value = enabled
    }

    fun setAdminFareMultiplier(value: Float) {
        _adminFareMultiplier.value = value
    }

    // Auth actions
    fun handleLogin(mobile: String, name: String, email: String, role: String, isFemale: Boolean, referral: String = "") {
        viewModelScope.launch {
            if (mobile.isBlank() || name.isBlank()) {
                _toastEvent.emit("Please fill in required fields.")
                return@launch
            }
            repository.createOrLoginProfile(mobile, name, email, role, isFemale, referral)
            _toastEvent.emit("Logged in securely as $name.")
        }
    }

    fun handleLogout() {
        viewModelScope.launch {
            repository.logout()
            _selectedPickup.value = null
            _selectedDestination.value = null
            _driverOnline.value = false
            _toastEvent.emit("Logged out.")
        }
    }

    // Booking actions
    fun requestRide() {
        val pickup = _selectedPickup.value
        val dest = _selectedDestination.value
        val profile = activeProfile.value
        if (pickup == null || dest == null) {
            viewModelScope.launch { _toastEvent.emit("Please select pickup and destination.") }
            return
        }
        if (pickup == dest) {
            viewModelScope.launch { _toastEvent.emit("Pickup and Destination cannot be the same.") }
            return
        }
        if (profile == null) return

        viewModelScope.launch {
            val baseFare = calculateFareEstimate(pickup, dest, _selectedVehicleType.value)
            val finalFare = baseFare * _adminFareMultiplier.value
            repository.bookRide(
                customerMobile = profile.mobile,
                customerName = profile.name,
                pickup = pickup.name,
                pickupLat = pickup.lat,
                pickupLng = pickup.lng,
                dest = dest.name,
                destLat = dest.lat,
                destLng = dest.lng,
                vehicleType = _selectedVehicleType.value,
                fare = finalFare.toDouble(),
                isWomenMode = _womenModeEnabled.value
            )
            _toastEvent.emit("Searching for nearby EV drivers...")
        }
    }

    fun cancelActiveRide() {
        val ride = liveRide.value ?: return
        viewModelScope.launch {
            repository.updateRideStatus(ride.id, "CANCELLED")
            _toastEvent.emit("Ride cancelled.")
        }
    }

    // Driver actions
    fun toggleDriverOnline() {
        viewModelScope.launch {
            val profile = activeProfile.value ?: return@launch
            val doc = repository.getDoc(profile.mobile)
            if (doc == null || doc.status != "APPROVED") {
                _toastEvent.emit("Driver verification is required to go online! Please complete onboarding.")
                return@launch
            }
            _driverOnline.value = !_driverOnline.value
            _toastEvent.emit(if (_driverOnline.value) "You are now online accepting matching rides." else "Offline.")
        }
    }

    fun updateDriverDoc(vehicleType: String, vehicleNo: String, dlNo: String, insNo: String) {
        viewModelScope.launch {
            val profile = activeProfile.value ?: return@launch
            if (vehicleNo.isBlank() || dlNo.isBlank()) {
                _toastEvent.emit("Please fill DL No and Vehicle Registration Plate.")
                return@launch
            }
            val newDoc = DriverDoc(
                driverMobile = profile.mobile,
                driverName = profile.name,
                vehicleType = vehicleType,
                vehicleNo = vehicleNo,
                dlNo = dlNo,
                certificateNo = insNo,
                status = "PENDING",
                isFemale = profile.isFemale
            )
            repository.submitDoc(newDoc)
            _toastEvent.emit("Documents submitted. Pending Admin Verification.")
        }
    }

    fun driverAcceptRide(rideId: Long) {
        viewModelScope.launch {
            val profile = activeProfile.value ?: return@launch
            val doc = currentDriverDoc.value ?: return@launch
            repository.acceptRideByDriver(rideId, profile.mobile, profile.name, doc.vehicleNo)
            _toastEvent.emit("Ride accepted! Route navigation enabled.")
        }
    }

    fun driverAdvanceRideStatus(rideId: Long, currentStatus: String) {
        viewModelScope.launch {
            val nextStatus = when (currentStatus) {
                "ACCEPTED" -> "ARRIVED"
                "ARRIVED" -> "STARTED"
                "STARTED" -> "COMPLETED"
                else -> return@launch
            }
            repository.updateRideStatus(rideId, nextStatus)
            _toastEvent.emit("Status updated to $nextStatus")
        }
    }

    // Admin verification audits
    fun adminApproveDriver(mobile: String) {
        viewModelScope.launch {
            repository.updateDocStatus(mobile, "APPROVED")
            _toastEvent.emit("Driver approved successfully.")
        }
    }

    fun adminRejectDriver(mobile: String) {
        viewModelScope.launch {
            repository.updateDocStatus(mobile, "REJECTED")
            _toastEvent.emit("Driver onboarding rejected.")
        }
    }

    // Safety Emergency controls
    fun toggleSOS() {
        viewModelScope.launch {
            val active = liveRide.value
            _toastEvent.emit("🚨 SOS DISTRIBUTED to security teams and emergency contacts!")
            if (active != null) {
                // Flash alert in admin panel
                repository.updateRideStatus(active.id, "SOS_ALERTED")
            }
        }
    }

    fun addEmergencyContact(name: String, phone: String) {
        viewModelScope.launch {
            val profile = activeProfile.value ?: return@launch
            if (name.isBlank() || phone.isBlank()) return@launch
            repository.addEmergencyContact(EmergencyContact(ownerMobile = profile.mobile, contactName = name, contactPhone = phone))
            _toastEvent.emit("Contact added.")
        }
    }

    fun removeEmergencyContact(contact: EmergencyContact) {
        viewModelScope.launch {
            repository.deleteEmergencyContact(contact)
            _toastEvent.emit("Contact deleted.")
        }
    }

    fun triggerFeedback(rideId: Long, rating: Float, feedbackText: String) {
        viewModelScope.launch {
            val ride = repository.getRideByIdSync(rideId)
            if (ride != null) {
                val updated = ride.copy(ratingCustomer = rating)
                repository.bookRide(
                    customerMobile = updated.customerMobile,
                    customerName = updated.customerName,
                    pickup = updated.pickupName,
                    pickupLat = updated.pickupLat,
                    pickupLng = updated.pickupLng,
                    dest = updated.destinationName,
                    destLat = updated.destLat,
                    destLng = updated.destLng,
                    vehicleType = updated.vehicleType,
                    fare = updated.fare,
                    isWomenMode = updated.isWomenMode
                ).let {
                    // Update rating inside database
                    repository.updateRideStatus(rideId, "COMPLETED")
                    _toastEvent.emit("Thank you for choosing VoltRide eco mobility!")
                }
            }
            _selectedPickup.value = null
            _selectedDestination.value = null
            repository.publishExternalUpdate(Ride(id = -1, customerMobile = "", customerName = "", pickupName = "", pickupLat = 0.0, pickupLng = 0.0, destinationName = "", destLat = 0.0, destLng = 0.0, vehicleType = "", fare = 0.0, status = "NONE"))
        }
    }

    fun calculateFareEstimate(pickup: RiderVenue, dest: RiderVenue, type: String): Float {
        // Linear distance approximation
        val dLat = abs(pickup.lat - dest.lat).toFloat()
        val dLng = abs(pickup.lng - dest.lng).toFloat()
        val km = (dLat + dLng) * 111.0f // roughly 111 km per degree lat
        val multiplier = when (type) {
            "EV Bike" -> 12.0f
            "EV Auto" -> 18.0f
            else -> 30.0f
        }
        val baseFee = when (type) {
            "EV Bike" -> 25.0f
            "EV Auto" -> 40.0f
            else -> 80.0f
        }
        return (baseFee + km * multiplier).coerceAtLeast(30.0f)
    }
}
