package com.example.data.repository

import android.content.Context
import android.util.Log
import com.example.data.model.Ride
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.firstOrNull

object Simulator {
    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private var job: Job? = null
    private var isSimulating = false

    fun start(context: Context) {
        if (isSimulating) return
        isSimulating = true
        val repo = VoltRideRepository.getRepository(context)

        job = scope.launch {
            while (isActive) {
                try {
                    val activeProfile = repo.activeProfile.firstOrNull()
                    val allRides = repo.getAllRides().firstOrNull() ?: emptyList()
                    val requestedRide = allRides.firstOrNull { it.status == "REQUESTED" }

                    if (requestedRide != null) {
                        // If the logged-in user is a Driver, let them accept manually themselves.
                        // If the logged-in user is a Customer or Admin, simulate a driver accepted workflow!
                        if (activeProfile == null || activeProfile.role != "DRIVER") {
                            // Run the simulation workflow for this ride
                            scope.launch {
                                simulateRideLifecycle(repo, requestedRide)
                            }
                        }
                    }

                    // Move around seed drivers slightly on the map just to simulate traffic and movement!
                    simulateMockDriverPatrol(repo)

                } catch (e: Exception) {
                    Log.e("Simulator", "Error in simulation loop", e)
                }
                delay(4000)
            }
        }
    }

    private fun simulateMockDriverPatrol(repo: VoltRideRepository) {
        val drivers = repo.onlineDrivers.value
        drivers.forEach { driver ->
            // Shift coordinates ever so slightly (-0.0003 to +0.0003)
            driver.lat += ((-150..150).random().toDouble() / 1000000.0)
            driver.lng += ((-150..150).random().toDouble() / 1000000.0)
        }
    }

    private suspend fun simulateRideLifecycle(repo: VoltRideRepository, requestedRide: Ride) {
        // Step 1: Wait 3 seconds, match simulated driver based on Women Mode if enabled
        delay(3000)
        
        // Double check if still requested
        val active = repo.getRideByIdSync(requestedRide.id) ?: return
        if (active.status != "REQUESTED") return

        // Find suitable simulated driver
        val driver = repo.onlineDrivers.value.firstOrNull {
            if (active.isWomenMode) it.isFemale else true
        } ?: repo.onlineDrivers.value.first()

        // Match driver and set status ACCEPTED
        repo.acceptRideByDriver(active.id, driver.vehicleNo, driver.name, driver.vehicleNo)

        // Step 2: Simulate movement from driver location to Pickup
        simulateDriverMovement(repo, active.id, driver.lat, driver.lng, active.pickupLat, active.pickupLng, "ARRIVED")
        
        // Step 3: Wait at pickup
        delay(3000)
        val afterArrived = repo.getRideByIdSync(active.id)
        if (afterArrived != null && afterArrived.status == "ARRIVED") {
            repo.updateRideStatus(active.id, "STARTED")
        }

        // Step 4: Simulate movement from Pickup to Destination
        simulateDriverMovement(repo, active.id, active.pickupLat, active.pickupLng, active.destLat, active.destLng, "COMPLETED")
    }

    private suspend fun simulateDriverMovement(
        repo: VoltRideRepository,
        rideId: Long,
        startLat: Double,
        startLng: Double,
        endLat: Double,
        endLng: Double,
        finalStatus: String
    ) {
        val steps = 8
        for (i in 1..steps) {
            delay(2000)
            val currentRide = repo.getRideByIdSync(rideId) ?: break
            if (currentRide.status == "CANCELLED" || currentRide.status == "COMPLETED") break

            val progress = i.toDouble() / steps.toDouble()
            val nextLat = startLat + (endLat - startLat) * progress
            val nextLng = startLng + (endLng - startLng) * progress

            repo.updateDriverLocation(rideId, nextLat, nextLng)
        }

        // Apply final status
        val currentRide = repo.getRideByIdSync(rideId)
        if (currentRide != null && currentRide.status != "CANCELLED" && currentRide.status != "COMPLETED") {
            repo.updateRideStatus(rideId, finalStatus)
        }
    }
}
