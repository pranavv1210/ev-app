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
import com.example.ui.VoltViewModel
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(viewModel: VoltViewModel) {
    val activeProfile by viewModel.activeProfile.collectAsState()
    val allRides by viewModel.allRides.collectAsState()
    val allDocs by viewModel.allDocs.collectAsState()
    val onlineDrivers by viewModel.onlineDrivers.collectAsState()
    val fareMultiplier by viewModel.adminFareMultiplier.collectAsState()

    var activeTab by remember { mutableStateOf("dashboard") } // dashboard, compliance, pricing

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = CardMidnight,
                tonalElevation = 8.dp,
                modifier = Modifier.navigationBarsPadding()
            ) {
                NavigationBarItem(
                    selected = activeTab == "dashboard",
                    onClick = { activeTab = "dashboard" },
                    icon = { Icon(Icons.Default.Dashboard, "Dashboard") },
                    label = { Text("Overview") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MidnightBlack,
                        selectedTextColor = ElectricMint,
                        indicatorColor = ElectricMint,
                        unselectedIconColor = SlateGray,
                        unselectedTextColor = SlateGray
                    )
                )
                NavigationBarItem(
                    selected = activeTab == "compliance",
                    onClick = { activeTab = "compliance" },
                    icon = { Icon(Icons.Default.VerifiedUser, "Compliance") },
                    label = { Text("Audit Docs") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MidnightBlack,
                        selectedTextColor = ElectricMint,
                        indicatorColor = ElectricMint,
                        unselectedIconColor = SlateGray,
                        unselectedTextColor = SlateGray
                    )
                )
                NavigationBarItem(
                    selected = activeTab == "pricing",
                    onClick = { activeTab = "pricing" },
                    icon = { Icon(Icons.Default.TrendingUp, "Pricing") },
                    label = { Text("Surge Controls") },
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
                "dashboard" -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Header
                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text("VoltRide Administrator", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = ElectricMint, letterSpacing = 2.sp)
                                    Text("Control Dashboard", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = IcyWhite)
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
                        }

                        // SOS Broadcast banner if any alert exists
                        val activeSOS = allRides.firstOrNull { it.status == "SOS_ALERTED" }
                        if (activeSOS != null) {
                            item {
                                Card(
                                    colors = CardDefaults.cardColors(containerColor = CrimsonAlert.copy(alpha = 0.25f)),
                                    shape = RoundedCornerShape(16.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .border(2.dp, CrimsonAlert, RoundedCornerShape(16.dp))
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(Icons.Default.Warning, null, tint = CrimsonAlert, modifier = Modifier.size(32.dp))
                                            Spacer(modifier = Modifier.width(12.dp))
                                            Text("CRITICAL: PASSENGER SOS ACTIVE!", color = CrimsonAlert, fontWeight = FontWeight.Black, fontSize = 14.sp)
                                        }
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text("Rider Mobile: ${activeSOS.customerMobile}", color = IcyWhite, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                        Text("Current coordinates: Lat ${"%.4f".format(activeSOS.driverLat ?: activeSOS.pickupLat)}, Lng ${"%.4f".format(activeSOS.driverLng ?: activeSOS.pickupLng)}", color = SlateGray, fontSize = 11.sp)
                                        Spacer(modifier = Modifier.height(10.dp))
                                        Button(
                                            onClick = { viewModel.driverAdvanceRideStatus(activeSOS.id, "COMPLETED") },
                                            colors = ButtonDefaults.buttonColors(containerColor = ElectricMint),
                                            modifier = Modifier.align(Alignment.End)
                                        ) {
                                            Text("Mark Secure / Resolve SOS", color = MidnightBlack, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                            }
                        }

                        // Operational Grid stats
                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Card(
                                    colors = CardDefaults.cardColors(containerColor = CardMidnight),
                                    modifier = Modifier
                                        .weight(1f)
                                        .border(1.dp, DeepGray, RoundedCornerShape(12.dp)),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Text("Active EV Rides", color = SlateGray, fontSize = 10.sp)
                                        Text("${allRides.count { it.status != "CANCELLED" && it.status != "COMPLETED" }} Ongoing", color = IcyWhite, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                    }
                                }

                                Card(
                                    colors = CardDefaults.cardColors(containerColor = CardMidnight),
                                    modifier = Modifier
                                        .weight(1f)
                                        .border(1.dp, DeepGray, RoundedCornerShape(12.dp)),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Text("CO2 Saved Today", color = SlateGray, fontSize = 10.sp)
                                        Text("%.1f KG".format(allRides.filter { it.status == "COMPLETED" }.sumOf { it.co2SavedKg }), color = ElectricMint, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }

                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Card(
                                    colors = CardDefaults.cardColors(containerColor = CardMidnight),
                                    modifier = Modifier
                                        .weight(1f)
                                        .border(1.dp, DeepGray, RoundedCornerShape(12.dp)),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Text("Online EV Fleets", color = SlateGray, fontSize = 10.sp)
                                        Text("${onlineDrivers.size} Active GPS", color = VoltYellow, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                    }
                                }

                                Card(
                                    colors = CardDefaults.cardColors(containerColor = CardMidnight),
                                    modifier = Modifier
                                        .weight(1f)
                                        .border(1.dp, DeepGray, RoundedCornerShape(12.dp)),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Text("Total Revenue", color = SlateGray, fontSize = 10.sp)
                                        Text("₹%.0f".format(allRides.filter { it.status == "COMPLETED" }.sumOf { it.fare }), color = IcyWhite, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }

                        // Recent requests monitoring list
                        item {
                            Text("Recent Ride Requests Telemetry", color = IcyWhite, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }

                        if (allRides.isEmpty()) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(140.dp),
                                    contentAlignment = Alignment.Center
                                    ) {
                                    Text("No ride telemetries reported yet.", color = SlateGray, fontSize = 12.sp)
                                }
                            }
                        } else {
                            items(allRides.take(5)) { ride ->
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
                                            Text("Rider: ${ride.customerName}", color = IcyWhite, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                            Text("${ride.pickupName} -> ${ride.destinationName}", color = SlateGray, fontSize = 10.sp)
                                            Text("Category: ${ride.vehicleType} | Status: ${ride.status}", color = SlateGray, fontSize = 9.sp)
                                        }
                                        Column(horizontalAlignment = Alignment.End) {
                                            Text("₹%.0f".format(ride.fare), color = ElectricMint, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                "compliance" -> {
                    // Driver Verification Audit Portal
                    val pendingDocs = allDocs.filter { it.status == "PENDING" }
                    val approvedDocs = allDocs.filter { it.status == "APPROVED" }

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        item {
                            Text("Compliance & Registrations", color = IcyWhite, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
                            Text("Verify commercial carrier permits to unlock driver duties.", color = SlateGray, fontSize = 12.sp)
                        }

                        item {
                            Text("Pending Audit Applications (${pendingDocs.size})", color = VoltYellow, fontSize = 14.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 8.dp))
                        }

                        if (pendingDocs.isEmpty()) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(100.dp)
                                        .background(DeepGray, RoundedCornerShape(12.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("All standard commercial applications verified and cleared.", color = SlateGray, fontSize = 12.sp)
                                }
                            }
                        } else {
                            items(pendingDocs) { doc ->
                                Card(
                                    colors = CardDefaults.cardColors(containerColor = CardMidnight),
                                    shape = RoundedCornerShape(14.dp),
                                    modifier = Modifier.fillMaxWidth().border(1.dp, DeepGray, RoundedCornerShape(14.dp))
                                ) {
                                    Column(modifier = Modifier.padding(14.dp)) {
                                        Text("Driver: ${doc.driverName}", color = IcyWhite, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                        Text("Mobile Key: ${doc.driverMobile}", color = SlateGray, fontSize = 11.sp)
                                        Text("Category: ${doc.vehicleType} | RC: ${doc.vehicleNo}", color = IcyWhite, fontSize = 12.sp)
                                        Text("Driving License Key: ${doc.dlNo}", color = SlateGray, fontSize = 11.sp)
                                        Text("Insurance Certificate: ${doc.certificateNo}", color = SlateGray, fontSize = 11.sp)

                                        Spacer(modifier = Modifier.height(12.dp))

                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            Button(
                                                onClick = { viewModel.adminApproveDriver(doc.driverMobile) },
                                                colors = ButtonDefaults.buttonColors(containerColor = ElectricMint),
                                                modifier = Modifier.weight(1f).testTag("approve_driver_${doc.driverMobile}"),
                                                shape = RoundedCornerShape(8.dp)
                                            ) {
                                                Text("APPROVE RIDES", color = MidnightBlack, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                            }

                                            Button(
                                                onClick = { viewModel.adminRejectDriver(doc.driverMobile) },
                                                colors = ButtonDefaults.buttonColors(containerColor = CrimsonAlert),
                                                modifier = Modifier.weight(1f).testTag("reject_driver_${doc.driverMobile}"),
                                                shape = RoundedCornerShape(8.dp)
                                            ) {
                                                Text("REJECT APPLICATION", color = IcyWhite, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        item {
                            Text("Verified Active EV Fleets (${approvedDocs.size})", color = ElectricMint, fontSize = 14.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 16.dp))
                        }

                        if (approvedDocs.isEmpty()) {
                            item {
                                Text("No approved drivers on platform yet.", color = SlateGray, fontSize = 11.sp)
                            }
                        } else {
                            items(approvedDocs) { doc ->
                                Card(
                                    colors = CardDefaults.cardColors(containerColor = DeepGray),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier.padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(Icons.Default.Verified, null, tint = ElectricMint)
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Column {
                                            Text(doc.driverName, color = IcyWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                            Text("${doc.vehicleType} (${doc.vehicleNo})", color = SlateGray, fontSize = 11.sp)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                "pricing" -> {
                    // Surge Control panel
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        Text("Surge Fare Management", color = IcyWhite, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
                        Text("Real-time pricing adjustments during rain, flight blockages, or rush hour.", color = SlateGray, fontSize = 12.sp)

                        Spacer(modifier = Modifier.height(24.dp))

                        Card(
                            colors = CardDefaults.cardColors(containerColor = CardMidnight),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, DeepGray, RoundedCornerShape(16.dp))
                                .padding(20.dp)
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Platform Surge Multiplier Rate", color = IcyWhite, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    "%.2fX".format(fareMultiplier),
                                    fontSize = 42.sp,
                                    fontWeight = FontWeight.Black,
                                    color = if (fareMultiplier > 1.0f) VoltYellow else ElectricMint
                                )
                                Text("Fares currently: ${if (fareMultiplier > 1.0f) "Surge hike active" else "Standard base price"}", color = SlateGray, fontSize = 11.sp)
                                
                                Spacer(modifier = Modifier.height(20.dp))

                                // Multiplier speed buttons
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    listOf(1.0f, 1.25f, 1.5f, 2.0f).forEach { rate ->
                                        val isSelected = fareMultiplier == rate
                                        Button(
                                            onClick = { viewModel.setAdminFareMultiplier(rate) },
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = if (isSelected) ElectricMint else DeepGray
                                            ),
                                            shape = RoundedCornerShape(8.dp),
                                            modifier = Modifier.weight(1f).testTag("surge_rate_${rate}")
                                        ) {
                                            Text("%.2f".format(rate), color = if (isSelected) MidnightBlack else IcyWhite, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
