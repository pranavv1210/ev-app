package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.EmergencyContact
import com.example.data.model.Ride
import com.example.data.repository.SimulatedDriver
import com.example.ui.RiderVenue
import com.example.ui.VoltViewModel
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.sin

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerScreen(viewModel: VoltViewModel) {
    val activeProfile by viewModel.activeProfile.collectAsState()
    val selectedPickup by viewModel.selectedPickup.collectAsState()
    val selectedDestination by viewModel.selectedDestination.collectAsState()
    val selectedVehicleType by viewModel.selectedVehicleType.collectAsState()
    val womenModeEnabled by viewModel.womenModeEnabled.collectAsState()
    val liveRide by viewModel.liveRide.collectAsState()
    val allRides by viewModel.allRides.collectAsState()
    val onlineDrivers by viewModel.onlineDrivers.collectAsState()
    val emergencyContacts by viewModel.emergencyContacts.collectAsState()
    val fareMultiplier by viewModel.adminFareMultiplier.collectAsState()

    var activeTab by remember { mutableStateOf("booking") } // booking, carbon, safety, history
    var showPickupDropdown by remember { mutableStateOf(false) }
    var showDestDropdown by remember { mutableStateOf(false) }

    // State for Emergency Contact add form
    var isAddingContact by remember { mutableStateOf(false) }
    var newContactName by remember { mutableStateOf("") }
    var newContactPhone by remember { mutableStateOf("") }

    // Rating dialogue
    var isRatingVisible by remember { mutableStateOf(false) }
    var userRating by remember { mutableStateOf(5f) }
    var userFeedback by remember { mutableStateOf("") }

    // Trigger ratings modal automatically once completed
    LaunchedEffect(liveRide?.status) {
        if (liveRide?.status == "COMPLETED") {
            isRatingVisible = true
        }
    }

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = CardMidnight,
                tonalElevation = 8.dp,
                modifier = Modifier.navigationBarsPadding()
            ) {
                NavigationBarItem(
                    selected = activeTab == "booking",
                    onClick = { activeTab = "booking" },
                    icon = { Icon(Icons.Default.Map, "Book") },
                    label = { Text("Book EV") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MidnightBlack,
                        selectedTextColor = ElectricMint,
                        indicatorColor = ElectricMint,
                        unselectedIconColor = SlateGray,
                        unselectedTextColor = SlateGray
                    )
                )
                NavigationBarItem(
                    selected = activeTab == "carbon",
                    onClick = { activeTab = "carbon" },
                    icon = { Icon(Icons.Default.Eco, "Carbon") },
                    label = { Text("Eco Stats") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MidnightBlack,
                        selectedTextColor = ElectricMint,
                        indicatorColor = ElectricMint,
                        unselectedIconColor = SlateGray,
                        unselectedTextColor = SlateGray
                    )
                )
                NavigationBarItem(
                    selected = activeTab == "safety",
                    onClick = { activeTab = "safety" },
                    icon = { Icon(Icons.Default.Shield, "Safety") },
                    label = { Text("Safety Hub") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MidnightBlack,
                        selectedTextColor = ElectricMint,
                        indicatorColor = ElectricMint,
                        unselectedIconColor = SlateGray,
                        unselectedTextColor = SlateGray
                    )
                )
                NavigationBarItem(
                    selected = activeTab == "history",
                    onClick = { activeTab = "history" },
                    icon = { Icon(Icons.Default.History, "History") },
                    label = { Text("History") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MidnightBlack,
                        selectedTextColor = ElectricMint,
                        indicatorColor = ElectricMint,
                        unselectedIconColor = SlateGray,
                        unselectedTextColor = SlateGray
                    )
                )
            }
        },
        containerColor = MidnightBlack
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (activeTab) {
                "booking" -> {
                    Column(modifier = Modifier.fillMaxSize()) {
                        // Custom Interactive Map Surface
                        Box(
                            modifier = Modifier
                                .weight(1.1f)
                                .fillMaxWidth()
                                .background(MidnightBlack)
                        ) {
                            RiderMapCanvas(
                                pickup = selectedPickup,
                                dest = selectedDestination,
                                liveRide = liveRide,
                                drivers = onlineDrivers,
                                modifier = Modifier.fillMaxSize()
                            )

                            // Quick Profile Switch Indicator
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                                    .statusBarsPadding(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Button(
                                    onClick = { viewModel.handleLogout() },
                                    colors = ButtonDefaults.buttonColors(containerColor = CardMidnight),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.border(1.dp, DeepGray, RoundedCornerShape(12.dp))
                                ) {
                                    Icon(Icons.Default.Logout, null, tint = CrimsonAlert)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Logout", color = IcyWhite, fontSize = 12.sp)
                                }

                                Card(
                                    colors = CardDefaults.cardColors(containerColor = CardMidnight),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.border(1.dp, DeepGray, RoundedCornerShape(12.dp))
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(8.dp)
                                                .clip(CircleShape)
                                                .background(ElectricMint)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "Rider: ${activeProfile?.name ?: ""}",
                                            color = IcyWhite,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }

                        // Booking Sheet Options
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                                .background(CardMidnight, RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                                .border(1.dp, DeepGray, RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                                .padding(20.dp)
                        ) {
                            if (liveRide != null && liveRide?.status != "CANCELLED" && liveRide?.status != "COMPLETED" && liveRide?.id != -1L) {
                                // Dynamic Live Trip Progression Sheet
                                ActiveTripPanel(liveRide!!, onCancel = { viewModel.cancelActiveRide() }, onTriggerSOS = { viewModel.toggleSOS() })
                            } else {
                                // Default Booking Form
                                BookingPanel(
                                    venues = viewModel.venues,
                                    selectedPickup = selectedPickup,
                                    selectedDestination = selectedDestination,
                                    selectedVehicleType = selectedVehicleType,
                                    womenModeEnabled = womenModeEnabled,
                                    showPickupDropdown = showPickupDropdown,
                                    showDestDropdown = showDestDropdown,
                                    fareMultiplier = fareMultiplier,
                                    onPickupSelect = { viewModel.selectPickup(it); showPickupDropdown = false },
                                    onDestSelect = { viewModel.selectDestination(it); showDestDropdown = false },
                                    onVehicleSelect = { viewModel.selectVehicleType(it) },
                                    onWomenModeToggle = { viewModel.setWomenMode(it) },
                                    onRequest = { viewModel.requestRide() },
                                    togglePickup = { showPickupDropdown = !showPickupDropdown },
                                    toggleDest = { showDestDropdown = !showDestDropdown }
                                )
                            }
                        }
                    }
                }
                "carbon" -> {
                    CarbonDashboard(activeProfile?.carbonSavedTotal ?: 0.0, allRides)
                }
                "safety" -> {
                    SafetyHub(
                        contacts = emergencyContacts,
                        isAdding = isAddingContact,
                        nameInput = newContactName,
                        phoneInput = newContactPhone,
                        onAddToggle = { isAddingContact = !isAddingContact },
                        onNameChange = { newContactName = it },
                        onPhoneChange = { newContactPhone = it },
                        onSave = {
                            viewModel.addEmergencyContact(newContactName, newContactPhone)
                            newContactName = ""
                            newContactPhone = ""
                            isAddingContact = false
                        },
                        onDelete = { viewModel.removeEmergencyContact(it) },
                        onTriggerSOS = { viewModel.toggleSOS() }
                    )
                }
                "history" -> {
                    RidesHistoryPanel(allRides)
                }
            }

            // Post-Trip Ratings Modal Dialogue
            if (isRatingVisible && liveRide != null) {
                AlertDialog(
                    onDismissRequest = { isRatingVisible = false },
                    containerColor = CardMidnight,
                    modifier = Modifier.border(1.dp, DeepGray, RoundedCornerShape(20.dp)),
                    title = {
                        Text("Rate your Zero Emission Trip", color = IcyWhite, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    },
                    text = {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("Your trip with ${liveRide?.driverName ?: "Simulated Driver"} completed!", color = SlateGray, fontSize = 12.sp, modifier = Modifier.padding(bottom = 12.dp))
                            
                            // Visual Stars Row
                            Row(
                                modifier = Modifier.padding(bottom = 16.dp),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                (1..5).forEach { index ->
                                    val isSelected = index <= userRating
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = null,
                                        tint = if (isSelected) VoltYellow else SlateGray,
                                        modifier = Modifier
                                            .size(36.dp)
                                            .clickable { userRating = index.toFloat() }
                                            .padding(2.dp)
                                    )
                                }
                            }

                            OutlinedTextField(
                                value = userFeedback,
                                onValueChange = { userFeedback = it },
                                label = { Text("Help us improve (Optional)", color = SlateGray) },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = IcyWhite,
                                    unfocusedTextColor = IcyWhite,
                                    focusedBorderColor = ElectricMint,
                                    unfocusedBorderColor = DeepGray
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                viewModel.triggerFeedback(liveRide!!.id, userRating, userFeedback)
                                isRatingVisible = false
                                userFeedback = ""
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = ElectricMint)
                        ) {
                            Text("Submit Eco Feedback", color = MidnightBlack)
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { isRatingVisible = false }) {
                            Text("Skip", color = SlateGray)
                        }
                    }
                )
            }
        }
    }
}

// Map rendering with high fidelity Custom geometric lines
@Composable
fun RiderMapCanvas(
    pickup: RiderVenue?,
    dest: RiderVenue?,
    liveRide: Ride?,
    drivers: List<SimulatedDriver>,
    modifier: Modifier
) {
    var tickPulse by remember { mutableStateOf(0f) }

    LaunchedEffect(Unit) {
        while (true) {
            tickPulse += 0.05f
            if (tickPulse > 1f) tickPulse = 0f
            delay(100)
        }
    }

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height

        // Clean slate layout drawing abstract grid lanes
        val spacing = 80.dp.toPx()
        val pathBrush = Brush.linearGradient(colors = listOf(MidnightBlack, DeepGray))

        // Draw street network grids (representing Bengaluru routes)
        for (i in 0..(width / spacing).toInt()) {
            drawLine(
                color = DeepGray,
                start = Offset(i * spacing, 0f),
                end = Offset(i * spacing, height),
                strokeWidth = 1.dp.toPx()
            )
        }
        for (j in 0..(height / spacing).toInt()) {
            drawLine(
                color = DeepGray,
                start = Offset(0f, j * spacing),
                end = Offset(width, j * spacing),
                strokeWidth = 1.dp.toPx()
            )
        }

        // Project coordinate math on Bangalore standard boundaries (roughly center: 12.97, 77.59)
        val centerLat = 12.95
        val centerLng = 77.62
        val scale = 14000f

        fun toOffset(lat: Double, lng: Double): Offset {
            val x = (width / 2) + (lng - centerLng) * scale
            val y = (height / 2) - (lat - centerLat) * scale
            return Offset(x.toFloat(), y.toFloat())
        }

        // Draw nearby active EV drivers dynamically patrolling
        drivers.forEach { drv ->
            val pos = toOffset(drv.lat, drv.lng)
            if (pos.x in 0f..width && pos.y in 0f..height) {
                // Circle halo representing active connection
                drawCircle(
                    color = ElectricMint.copy(alpha = 0.2f),
                    radius = 16.dp.toPx(),
                    center = pos
                )
                drawCircle(
                    color = ElectricMint,
                    radius = 5.dp.toPx(),
                    center = pos
                )
            }
        }

        // Draw pickup and destination lines/markers
        if (pickup != null) {
            val pPos = toOffset(pickup.lat, pickup.lng)
            drawCircle(
                color = VoltYellow.copy(alpha = 0.3f * (1f - tickPulse)),
                radius = 32.dp.toPx() * tickPulse,
                center = pPos,
                style = Stroke(width = 2.dp.toPx())
            )
            drawCircle(color = VoltYellow, radius = 8.dp.toPx(), center = pPos)
            
            if (dest != null) {
                val dPos = toOffset(dest.lat, dest.lng)
                // Draw polyline path
                drawLine(
                    color = ElectricMint,
                    start = pPos,
                    end = dPos,
                    strokeWidth = 3.dp.toPx(),
                    alpha = 0.7f
                )
                drawCircle(color = CrimsonAlert, radius = 8.dp.toPx(), center = dPos)
            }
        }

        // Draw current Ride progress tracking
        if (liveRide != null && liveRide.driverLat != null && liveRide.driverLng != null) {
            val dlPos = toOffset(liveRide.driverLat, liveRide.driverLng)
            // Pulse circle
            drawCircle(
                color = BlueVolt.copy(alpha = 0.3f),
                radius = 24.dp.toPx() * tickPulse,
                center = dlPos,
                style = Stroke(width = 1.dp.toPx())
            )
            drawCircle(color = BlueVolt, radius = 10.dp.toPx(), center = dlPos)
        }
    }
}

@Composable
fun BookingPanel(
    venues: List<RiderVenue>,
    selectedPickup: RiderVenue?,
    selectedDestination: RiderVenue?,
    selectedVehicleType: String,
    womenModeEnabled: Boolean,
    showPickupDropdown: Boolean,
    showDestDropdown: Boolean,
    fareMultiplier: Float,
    onPickupSelect: (RiderVenue) -> Unit,
    onDestSelect: (RiderVenue) -> Unit,
    onVehicleSelect: (String) -> Unit,
    onWomenModeToggle: (Boolean) -> Unit,
    onRequest: () -> Unit,
    togglePickup: () -> Unit,
    toggleDest: () -> Unit
) {
    Column {
        Text("Request EV Mobility", color = IcyWhite, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold)
        Spacer(modifier = Modifier.height(16.dp))

        // Venues Dropdown Selectors
        Box(modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = togglePickup,
                colors = ButtonDefaults.buttonColors(containerColor = DeepGray),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("pickup_selector_button"),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.LocationOn, null, tint = VoltYellow)
                Spacer(modifier = Modifier.width(8.dp))
                Text(selectedPickup?.name ?: "Select Pickup Venue", color = IcyWhite, fontSize = 13.sp)
                Spacer(modifier = Modifier.weight(1f))
                Icon(Icons.Default.ArrowDropDown, null, tint = SlateGray)
            }
            DropdownMenu(
                expanded = showPickupDropdown,
                onDismissRequest = togglePickup,
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .background(CardMidnight)
                    .border(1.dp, DeepGray)
            ) {
                venues.forEach { venue ->
                    DropdownMenuItem(
                        text = { Text(venue.name, color = IcyWhite) },
                        onClick = { onPickupSelect(venue) }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Box(modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = toggleDest,
                colors = ButtonDefaults.buttonColors(containerColor = DeepGray),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("dest_selector_button"),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.PinDrop, null, tint = CrimsonAlert)
                Spacer(modifier = Modifier.width(8.dp))
                Text(selectedDestination?.name ?: "Select Destination Venue", color = IcyWhite, fontSize = 13.sp)
                Spacer(modifier = Modifier.weight(1f))
                Icon(Icons.Default.ArrowDropDown, null, tint = SlateGray)
            }
            DropdownMenu(
                expanded = showDestDropdown,
                onDismissRequest = toggleDest,
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .background(CardMidnight)
                    .border(1.dp, DeepGray)
            ) {
                venues.forEach { venue ->
                    DropdownMenuItem(
                        text = { Text(venue.name, color = IcyWhite) },
                        onClick = { onDestSelect(venue) }
                    )
                }
            }
        }

        // Carbon Saved Estimation ticker
        if (selectedPickup != null && selectedDestination != null) {
            val co2SaveGrams = when (selectedVehicleType) {
                "EV Bike" -> 540
                "EV Auto" -> 810
                else -> 1125
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
                    .background(DeepMint.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Park, null, tint = ElectricMint)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Selecting $selectedVehicleType saves ~$co2SaveGrams grams of CO2 greenhouse gases!",
                    color = ElectricMint,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        } else {
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Vehicle Categories Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf(
                Triple("EV Bike", "₹12/km", Icons.Default.TwoWheeler),
                Triple("EV Auto", "₹18/km", Icons.Default.ElectricRickshaw),
                Triple("EV Cab", "₹30/km", Icons.Default.ElectricCar)
            ).forEach { (vType, rate, icon) ->
                val isSelected = selectedVehicleType == vType
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) DeepMint else DeepGray
                    ),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .weight(1f)
                        .border(
                            1.dp,
                            if (isSelected) ElectricMint else Color.Transparent,
                            RoundedCornerShape(16.dp)
                        )
                        .clickable { onVehicleSelect(vType) }
                        .testTag("vehicle_card_$vType")
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(icon, null, tint = if (isSelected) ElectricMint else SlateGray, modifier = Modifier.size(28.dp))
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(vType, color = IcyWhite, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        Text(rate, color = SlateGray, fontSize = 9.sp)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Dynamic pricing / Safety Toggles
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Female, null, tint = Color(0xFFFF69B4))
                Spacer(modifier = Modifier.width(6.dp))
                Column {
                    Text("Women Mode", color = IcyWhite, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Text("Female-only drivers matched", color = SlateGray, fontSize = 9.sp)
                }
            }
            Switch(
                checked = womenModeEnabled,
                onCheckedChange = { onWomenModeToggle(it) },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = ElectricMint,
                    checkedTrackColor = DeepMint,
                    uncheckedThumbColor = SlateGray,
                    uncheckedTrackColor = DeepGray
                ),
                modifier = Modifier.testTag("women_mode_switch")
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Request button
        Button(
            onClick = onRequest,
            colors = ButtonDefaults.buttonColors(containerColor = ElectricMint),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("request_ride_button"),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = if (fareMultiplier > 1.0f) "BOOK NOW (Premium Dynamic pricing active)" else "CONFIRM ZERO EMISSION TRIP",
                fontSize = 12.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MidnightBlack
            )
        }
    }
}

@Composable
fun ActiveTripPanel(ride: Ride, onCancel: () -> Unit, onTriggerSOS: () -> Unit) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Ride Status: ${ride.status}",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = ElectricMint
            )
            Spacer(modifier = Modifier.weight(1f))
            CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp, color = ElectricMint)
        }
        Spacer(modifier = Modifier.height(8.dp))

        Text("Pickup: ${ride.pickupName}", color = SlateGray, fontSize = 10.sp)
        Text("Drop-off: ${ride.destinationName}", color = SlateGray, fontSize = 10.sp)

        Spacer(modifier = Modifier.height(10.dp))

        // Driver matched stats
        Card(
            colors = CardDefaults.cardColors(containerColor = DeepGray),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(DeepMint),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.ElectricCar, null, tint = ElectricMint)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(ride.driverName ?: "Locating EV matches...", color = IcyWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Text(ride.driverVehicleNo ?: "Please stand by", color = SlateGray, fontSize = 11.sp)
                }
                Spacer(modifier = Modifier.weight(1f))
                Column(horizontalAlignment = Alignment.End) {
                    Text("OTP PIN", color = SlateGray, fontSize = 9.sp)
                    Text("4299", color = VoltYellow, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Safety SOS and Cancel Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = onTriggerSOS,
                colors = ButtonDefaults.buttonColors(containerColor = CrimsonAlert),
                modifier = Modifier
                    .weight(1f)
                    .height(44.dp)
                    .testTag("sos_alert_button"),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(Icons.Default.Warning, null, tint = IcyWhite)
                Spacer(modifier = Modifier.width(6.dp))
                Text("TRIGGER SOS", color = IcyWhite, fontWeight = FontWeight.Bold, fontSize = 11.sp)
            }

            Button(
                onClick = onCancel,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                modifier = Modifier
                    .weight(1f)
                    .height(44.dp)
                    .border(1.dp, CrimsonAlert, RoundedCornerShape(10.dp))
                    .testTag("cancel_ride_button"),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Cancel Ride", color = CrimsonAlert, fontSize = 11.sp)
            }
        }
    }
}

// Carbon Footprint Tracker view
@Composable
fun CarbonDashboard(totalSaved: Double, rides: List<Ride>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text("Your Eco Impact", color = IcyWhite, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
            Text("Every VoltRide choice removes internal-combustion emission footprints.", color = SlateGray, fontSize = 12.sp)
        }

        // Carbon Ticker Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = DeepMint.copy(alpha = 0.3f)),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(2.dp, ElectricMint, RoundedCornerShape(20.dp))
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(Icons.Default.CloudQueue, null, tint = ElectricMint, modifier = Modifier.size(48.dp))
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "%.2f KG".format(totalSaved),
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Black,
                        color = ElectricMint
                    )
                    Text("Total CO2 Greenhouse Gases Prevented", color = IcyWhite, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Equivalent to planting ${(totalSaved * 0.45).toInt()} mature cedar pine trees!", color = SlateGray, fontSize = 10.sp)
                }
            }
        }

        // Achievements List
        item {
            Text("Achievements & Badges", color = IcyWhite, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }

        item {
            val isL1 = totalSaved >= 0.5
            val isL2 = totalSaved >= 1.5
            val isL3 = totalSaved >= 3.0

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                listOf(
                    Triple("Green Spark", "Saved First 500g CO2", isL1),
                    Triple("Eco Cruiser", "Saved 1.5kg CO2", isL2),
                    Triple("Emissions Hero", "Saved 3kg CO2", isL3)
                ).forEach { (badge, desc, active) ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = if (active) CardMidnight else DeepGray),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .border(1.dp, if (active) VoltYellow else Color.Transparent, RoundedCornerShape(12.dp))
                    ) {
                        Column(
                            modifier = Modifier.padding(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = if (active) Icons.Default.EmojiEvents else Icons.Default.Lock,
                                contentDescription = null,
                                tint = if (active) VoltYellow else SlateGray,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(badge, color = IcyWhite, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            Text(desc, color = SlateGray, fontSize = 8.sp, textAlign = TextAlign.Center)
                        }
                    }
                }
            }
        }
    }
}

// Safety Hub directory manager
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SafetyHub(
    contacts: List<EmergencyContact>,
    isAdding: Boolean,
    nameInput: String,
    phoneInput: String,
    onAddToggle: () -> Unit,
    onNameChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onSave: () -> Unit,
    onDelete: (EmergencyContact) -> Unit,
    onTriggerSOS: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Emergency Safety Hub", color = IcyWhite, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
        Text("VoltRide puts passengers safety first with robust monitoring layers.", color = SlateGray, fontSize = 12.sp)

        Spacer(modifier = Modifier.height(16.dp))

        // Big Crimson SOS Alert Panel
        Card(
            colors = CardDefaults.cardColors(containerColor = CrimsonAlert.copy(alpha = 0.2f)),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, CrimsonAlert, RoundedCornerShape(16.dp))
                .clickable { onTriggerSOS() }
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Emergency, null, tint = CrimsonAlert, modifier = Modifier.size(36.dp))
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text("SOS INSTANT EMERGENCY FORCE", color = CrimsonAlert, fontWeight = FontWeight.ExtraBold, fontSize = 14.sp)
                    Text("Broadcast vehicle location and request security assistance.", color = IcyWhite, fontSize = 11.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Emergency Guardians", color = IcyWhite, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            IconButton(onClick = onAddToggle) {
                Icon(Icons.Default.AddCircle, null, tint = ElectricMint)
            }
        }

        if (isAdding) {
            Card(
                colors = CardDefaults.cardColors(containerColor = CardMidnight),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
                    .border(1.dp, DeepGray, RoundedCornerShape(12.dp))
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    TextField(
                        value = nameInput,
                        onValueChange = onNameChange,
                        label = { Text("Contact Name", color = SlateGray) },
                        colors = TextFieldDefaults.colors(focusedTextColor = IcyWhite, unfocusedTextColor = IcyWhite, focusedContainerColor = Color.Transparent, unfocusedContainerColor = Color.Transparent)
                    )
                    TextField(
                        value = phoneInput,
                        onValueChange = onPhoneChange,
                        label = { Text("Phone Number", color = SlateGray) },
                        colors = TextFieldDefaults.colors(focusedTextColor = IcyWhite, unfocusedTextColor = IcyWhite, focusedContainerColor = Color.Transparent, unfocusedContainerColor = Color.Transparent)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = onSave,
                        colors = ButtonDefaults.buttonColors(containerColor = ElectricMint),
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("Add Guardian", color = MidnightBlack)
                    }
                }
            }
        }

        if (contacts.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text("No safety contacts added yet. Click + to declare guardians.", color = SlateGray, fontSize = 12.sp, textAlign = TextAlign.Center)
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(contacts) { contact ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = DeepGray),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.PersonOutline, null, tint = ElectricMint)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(contact.contactName, color = IcyWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                Text(contact.contactPhone, color = SlateGray, fontSize = 11.sp)
                            }
                            Spacer(modifier = Modifier.weight(1f))
                            IconButton(onClick = { onDelete(contact) }) {
                                Icon(Icons.Default.DeleteOutline, null, tint = CrimsonAlert)
                            }
                        }
                    }
                }
            }
        }
    }
}

// User-Facing Historic logs panel
@Composable
fun RidesHistoryPanel(rides: List<Ride>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Your Trip Logs", color = IcyWhite, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
        Spacer(modifier = Modifier.height(12.dp))

        if (rides.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("No previous zero emission rides matching profile.", color = SlateGray, fontSize = 12.sp)
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(rides) { ride ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = CardMidnight),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth().border(1.dp, DeepGray, RoundedCornerShape(12.dp))
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Destination: ${ride.destinationName}", color = IcyWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                Text("Vehicle: ${ride.vehicleType} | Status: ${ride.status}", color = SlateGray, fontSize = 10.sp)
                                Text("Date: ${java.text.DateFormat.getDateTimeInstance().format(java.util.Date(ride.timestamp))}", color = SlateGray, fontSize = 9.sp)
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text("₹%.0f".format(ride.fare), color = ElectricMint, fontWeight = FontWeight.Black, fontSize = 14.sp)
                                Text("-%.1fg CO2".format(ride.co2SavedKg * 1000), color = VoltYellow, fontSize = 9.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}
