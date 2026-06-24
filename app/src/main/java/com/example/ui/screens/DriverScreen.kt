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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Ride
import com.example.ui.VoltViewModel
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DriverScreen(viewModel: VoltViewModel) {
    val activeProfile by viewModel.activeProfile.collectAsState()
    val driverDoc by viewModel.currentDriverDoc.collectAsState()
    val driverOnline by viewModel.driverOnline.collectAsState()
    val allRides by viewModel.allRides.collectAsState()
    val liveRide by viewModel.liveRide.collectAsState()

    var activeTab by remember { mutableStateOf("home") } // home, earnings, documents

    // Document form fields
    var vehicleType by remember { mutableStateOf("EV Cab") }
    var vehicleNo by remember { mutableStateOf("") }
    var dlNo by remember { mutableStateOf("") }
    var insNo by remember { mutableStateOf("") }

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = CardMidnight,
                tonalElevation = 8.dp,
                modifier = Modifier.navigationBarsPadding()
            ) {
                NavigationBarItem(
                    selected = activeTab == "home",
                    onClick = { activeTab = "home" },
                    icon = { Icon(Icons.Default.DriveEta, "Duty") },
                    label = { Text("Duty Console") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MidnightBlack,
                        selectedTextColor = ElectricMint,
                        indicatorColor = ElectricMint,
                        unselectedIconColor = SlateGray,
                        unselectedTextColor = SlateGray
                    )
                )
                NavigationBarItem(
                    selected = activeTab == "earnings",
                    onClick = { activeTab = "earnings" },
                    icon = { Icon(Icons.Default.Payments, "Earnings") },
                    label = { Text("Earnings") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MidnightBlack,
                        selectedTextColor = ElectricMint,
                        indicatorColor = ElectricMint,
                        unselectedIconColor = SlateGray,
                        unselectedTextColor = SlateGray
                    )
                )
                NavigationBarItem(
                    selected = activeTab == "documents",
                    onClick = { activeTab = "documents" },
                    icon = { Icon(Icons.Default.FolderOpen, "Docs") },
                    label = { Text("Credentials") },
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
                "home" -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        // Header
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Bengaluru Drive Console",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ElectricMint,
                                    letterSpacing = 1.5.sp
                                )
                                Text(
                                    text = "Duty active: ${activeProfile?.name ?: ""}",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = IcyWhite
                                )
                            }

                            IconButton(
                                onClick = { viewModel.handleLogout() },
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(DeepGray)
                            ) {
                                Icon(Icons.Default.Logout, "Logout", tint = CrimsonAlert)
                            }
                        }

                        // Verify status check
                        if (driverDoc == null) {
                            CredentialAlertPane { activeTab = "documents" }
                        } else if (driverDoc?.status == "PENDING") {
                            DocVerificationPendingPane()
                        } else if (driverDoc?.status == "REJECTED") {
                            DocRejectedPane { activeTab = "documents" }
                        } else {
                            // Online/Offline Duty Master Toggle
                            Card(
                                colors = CardDefaults.cardColors(containerColor = if (driverOnline) DeepMint.copy(alpha = 0.3f) else DeepGray),
                                shape = RoundedCornerShape(20.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { viewModel.toggleDriverOnline() }
                                    .border(
                                        1.dp,
                                        if (driverOnline) ElectricMint else DeepGray,
                                        RoundedCornerShape(20.dp)
                                    )
                                    .testTag("driver_online_card")
                            ) {
                                Row(
                                    modifier = Modifier.padding(20.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(12.dp)
                                            .clip(CircleShape)
                                            .background(if (driverOnline) ElectricMint else SlateGray)
                                    )
                                    Spacer(modifier = Modifier.width(16.dp))
                                    Column {
                                        Text(
                                            text = if (driverOnline) "ONLINE & BROADCASTING" else "CURRENTLY OFFLINE",
                                            fontWeight = FontWeight.Black,
                                            fontSize = 14.sp,
                                            color = if (driverOnline) ElectricMint else IcyWhite
                                        )
                                        Text(
                                            text = if (driverOnline) "Matches requested near Bangalore active" else "Tap to activate and start receiving premium fares",
                                            fontSize = 11.sp,
                                            color = SlateGray
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            // Active matched requested ride in immediate area
                            val incomingRide = allRides.firstOrNull { it.status == "REQUESTED" }
                            val myActiveRide = allRides.firstOrNull {
                                it.driverMobile == activeProfile?.mobile &&
                                        it.status != "CANCELLED" &&
                                        it.status != "COMPLETED"
                            } ?: liveRide?.let {
                                if (it.driverMobile == activeProfile?.mobile && it.status != "CANCELLED" && it.status != "COMPLETED") it else null
                            }

                            if (myActiveRide != null) {
                                Text("Your Active Trip Route", color = IcyWhite, fontWeight = FontWeight.Bold, fontSize = 16.sp, modifier = Modifier.padding(bottom = 12.dp))
                                ActiveJobCard(myActiveRide, onAdvanceStatus = {
                                    viewModel.driverAdvanceRideStatus(myActiveRide.id, myActiveRide.status)
                                })
                            } else if (incomingRide != null && driverOnline) {
                                Text("New EV Match Nearby!", color = ElectricMint, fontWeight = FontWeight.Bold, fontSize = 16.sp, modifier = Modifier.padding(bottom = 12.dp))
                                IncomingRequestCard(incomingRide, onAccept = {
                                    viewModel.driverAcceptRide(incomingRide.id)
                                })
                            } else {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .weight(1f)
                                        .background(CardMidnight, RoundedCornerShape(16.dp))
                                        .border(1.dp, DeepGray, RoundedCornerShape(16.dp)),
                                    contentAlignment = Alignment.Center
                                    ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Icon(Icons.Default.Radar, null, tint = SlateGray, modifier = Modifier.size(48.dp))
                                        Spacer(modifier = Modifier.height(12.dp))
                                        Text(
                                            text = if (driverOnline) "Pinging for zero emission requests..." else "Go online to receive EV matching fares",
                                            color = SlateGray,
                                            fontSize = 11.sp,
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                "earnings" -> {
                    val myTrips = allRides.filter { it.driverMobile == activeProfile?.mobile && it.status == "COMPLETED" }
                    val totalSum = myTrips.sumOf { it.fare }
                    
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        item {
                            Text("Earnings Ledger", color = IcyWhite, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
                            Text("Transparent pricing settlements with same-day UPI dispatch.", color = SlateGray, fontSize = 12.sp)
                        }

                        // Total earnings score
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
                                    Text("₹%.2f".format(totalSum), color = ElectricMint, fontSize = 36.sp, fontWeight = FontWeight.Black)
                                    Text("Settled Balance Today", color = IcyWhite, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    Text("Premium Platform fee (0%)", color = SlateGray, fontSize = 10.sp)
                                }
                            }
                        }

                        item {
                            Text("Recent Settled Trips", color = IcyWhite, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }

                        if (myTrips.isEmpty()) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(120.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("No completed settled trips logged yet.", color = SlateGray, fontSize = 12.sp)
                                }
                            }
                        } else {
                            items(myTrips) { ride ->
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
                                            Text(ride.customerName, color = IcyWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                            Text("${ride.pickupName} -> ${ride.destinationName}", color = SlateGray, fontSize = 10.sp)
                                        }
                                        Text("₹%.0f".format(ride.fare), color = ElectricMint, fontWeight = FontWeight.ExtraBold, fontSize = 14.sp)
                                    }
                                }
                            }
                        }
                    }
                }
                "documents" -> {
                    // Document Form
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp)
                    ) {
                        Text("Credential Verification", color = IcyWhite, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
                        Text("Earn extra with Bengaluru's premiere EV platform with direct customer rating matching.", color = SlateGray, fontSize = 12.sp)
                        
                        Spacer(modifier = Modifier.height(16.dp))

                        Card(
                            colors = CardDefaults.cardColors(containerColor = CardMidnight),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, DeepGray, RoundedCornerShape(16.dp))
                                .padding(16.dp)
                        ) {
                            Column {
                                Text("Onboarding Documents status", color = IcyWhite, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Spacer(modifier = Modifier.height(8.dp))
                                
                                val statusText = driverDoc?.status ?: "NOT SUBMITTED"
                                val statusColor = when (statusText) {
                                    "APPROVED" -> ElectricMint
                                    "PENDING" -> VoltYellow
                                    "REJECTED" -> CrimsonAlert
                                    else -> SlateGray
                                }
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(statusColor))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(statusText, color = statusColor, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                }
                            }
                        }

                        // Form fields
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Update Vehicles & License Key Profiles", color = IcyWhite, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(12.dp))

                        // Category Dropdown simulated
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf("EV Bike", "EV Auto", "EV Cab").forEach { type ->
                                val isSelected = vehicleType == type
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(if (isSelected) DeepMint else DeepGray)
                                        .border(1.dp, if (isSelected) ElectricMint else Color.Transparent, RoundedCornerShape(10.dp))
                                        .clickable { vehicleType = type }
                                        .padding(vertical = 10.dp)
                                ) {
                                    Text(type, color = if (isSelected) ElectricMint else IcyWhite, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        OutlinedTextField(
                            value = vehicleNo,
                            onValueChange = { vehicleNo = it },
                            label = { Text("Registration Vehicle Plate (e.g. KA-03-EM-1123)", color = SlateGray) },
                            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = IcyWhite, unfocusedTextColor = IcyWhite, focusedBorderColor = ElectricMint, unfocusedBorderColor = DeepGray),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp).testTag("doc_vehicle_input")
                        )

                        OutlinedTextField(
                            value = dlNo,
                            onValueChange = { dlNo = it },
                            label = { Text("Commercial Driving License (DL) Key", color = SlateGray) },
                            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = IcyWhite, unfocusedTextColor = IcyWhite, focusedBorderColor = ElectricMint, unfocusedBorderColor = DeepGray),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp).testTag("doc_dl_input")
                        )

                        OutlinedTextField(
                            value = insNo,
                            onValueChange = { insNo = it },
                            label = { Text("EV Fleet Third-party Insurance Certificate", color = SlateGray) },
                            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = IcyWhite, unfocusedTextColor = IcyWhite, focusedBorderColor = ElectricMint, unfocusedBorderColor = DeepGray),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp).testTag("doc_insurance_input")
                        )

                        Button(
                            onClick = { viewModel.updateDriverDoc(vehicleType, vehicleNo, dlNo, insNo) },
                            colors = ButtonDefaults.buttonColors(containerColor = ElectricMint),
                            modifier = Modifier.fillMaxWidth().height(48.dp).testTag("submit_docs_button"),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("SUBMIT FOR HYBRID VERIFICATION", color = MidnightBlack, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CredentialAlertPane(onClick: () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = CrimsonAlert.copy(alpha = 0.15f)),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, CrimsonAlert, RoundedCornerShape(16.dp))
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Verification Required", color = CrimsonAlert, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text("Please complete uploading your active Driving License and RC plate credentials to verify profile and accept rides.", color = IcyWhite, fontSize = 11.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text("TAP HERE TO ONBOARD ->", color = CrimsonAlert, fontWeight = FontWeight.Bold, fontSize = 11.sp)
        }
    }
}

@Composable
fun DocVerificationPendingPane() {
    Card(
        colors = CardDefaults.cardColors(containerColor = VoltYellow.copy(alpha = 0.15f)),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, VoltYellow, RoundedCornerShape(16.dp))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Onboarding verification pending", color = VoltYellow, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text("Our administrative backend auditors are checking your submitted insurance/DL certificates. Check back inside 10 minutes.", color = IcyWhite, fontSize = 11.sp)
        }
    }
}

@Composable
fun DocRejectedPane(onClick: () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = CrimsonAlert.copy(alpha = 0.15f)),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, CrimsonAlert, RoundedCornerShape(16.dp))
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Onboarding Rejected", color = CrimsonAlert, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text("Your driving license verification was rejected. Please re-submit clear documents.", color = IcyWhite, fontSize = 11.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Tap to fix onboarding errors.", color = CrimsonAlert, fontWeight = FontWeight.Bold, fontSize = 11.sp)
        }
    }
}

@Composable
fun IncomingRequestCard(ride: Ride, onAccept: () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = CardMidnight),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, ElectricMint, RoundedCornerShape(16.dp))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(DeepMint), contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Route, null, tint = ElectricMint)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(ride.customerName, color = IcyWhite, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text("Requested category: ${ride.vehicleType}", color = SlateGray, fontSize = 11.sp)
                }
                Spacer(modifier = Modifier.weight(1f))
                Text("₹%.0f".format(ride.fare), color = ElectricMint, fontWeight = FontWeight.Black, fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text("Pickup: ${ride.pickupName}", color = IcyWhite, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            Text("Drop-off: ${ride.destinationName}", color = SlateGray, fontSize = 11.sp)

            if (ride.isWomenMode) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Female, null, tint = Color(0xFFFF69B4), modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("WOMEN MODE MATCHMAKING ENABLED", color = Color(0xFFFF69B4), fontWeight = FontWeight.Bold, fontSize = 10.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onAccept,
                colors = ButtonDefaults.buttonColors(containerColor = ElectricMint),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
                    .testTag("driver_accept_button"),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("ACCEPT & LOCK RIDE FARE", color = MidnightBlack, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun ActiveJobCard(ride: Ride, onAdvanceStatus: () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = CardMidnight),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, DeepGray, RoundedCornerShape(16.dp))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("ROUTE ASSIGNMENT", color = ElectricMint, fontWeight = FontWeight.ExtraBold, fontSize = 11.sp)
                Spacer(modifier = Modifier.weight(1f))
                Text("Status: ${ride.status}", color = VoltYellow, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text("Rider: ${ride.customerName}", color = IcyWhite, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Text("Pickup location: ${ride.pickupName}", color = SlateGray, fontSize = 12.sp)
            Text("Destination target: ${ride.destinationName}", color = SlateGray, fontSize = 12.sp)

            Spacer(modifier = Modifier.height(16.dp))

            val (lbl, containerBg) = when (ride.status) {
                "ACCEPTED" -> "START COMMUTE (ARRIVED AT PICKUP)" to ElectricMint
                "ARRIVED" -> "BOARD PASSENGER (START TRIP)" to VoltYellow
                "STARTED" -> "RESOLVE TRIP (COMPLETE & ARRIVED)" to ElectricMint
                else -> "UPDATESTATUS" to ElectricMint
            }

            Button(
                onClick = onAdvanceStatus,
                colors = ButtonDefaults.buttonColors(containerColor = containerBg),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("driver_advance_button"),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(lbl, color = MidnightBlack, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
