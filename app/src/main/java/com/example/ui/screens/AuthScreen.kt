package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.VoltViewModel
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(viewModel: VoltViewModel) {
    var name by remember { mutableStateOf("") }
    var mobile by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var role by remember { mutableStateOf("CUSTOMER") } // CUSTOMER, DRIVER, ADMIN
    var isFemale by remember { mutableStateOf(false) }
    var referCode by remember { mutableStateOf("") }

    // Aesthetic design overlay background
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(MidnightBlack, DeepMint, MidnightBlack)
                )
            )
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .statusBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // VoltRide Brand Emblem
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ElectricBolt,
                    contentDescription = "VoltRide Bolt Logo",
                    tint = ElectricMint,
                    modifier = Modifier.size(42.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "VoltRide",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = IcyWhite,
                    letterSpacing = 2.sp
                )
            }

            Text(
                text = "PREMIUM EV MOBILE PLATFORM",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = ElectricMint,
                letterSpacing = 3.sp,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Auth Input Glass Card
            Card(
                colors = CardDefaults.cardColors(containerColor = CardMidnight),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, DeepGray, RoundedCornerShape(24.dp))
                    .padding(4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Initialize Premium Ride Sessions",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = IcyWhite,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // Role Switcher Cards
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 20.dp)
                            .background(DeepGray, RoundedCornerShape(12.dp))
                            .padding(4.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        listOf("CUSTOMER" to "Rider", "DRIVER" to "Driver", "ADMIN" to "Admin").forEach { (rType, rLabel) ->
                            val isSelected = role == rType
                            val bgColor = if (isSelected) ElectricMint else Color.Transparent
                            val txtColor = if (isSelected) MidnightBlack else SlateGray
                            
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(bgColor)
                                    .clickable { role = rType }
                                    .padding(vertical = 8.dp)
                                    .testTag("role_select_$rType")
                            ) {
                                Text(
                                    text = rLabel,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = txtColor
                                )
                            }
                        }
                    }

                    // Input Form
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Full Name", color = SlateGray) },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = ElectricMint) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = IcyWhite,
                            unfocusedTextColor = IcyWhite,
                            focusedBorderColor = ElectricMint,
                            unfocusedBorderColor = DeepGray
                        ),
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
                            .testTag("name_input"),
                        shape = RoundedCornerShape(12.dp)
                    )

                    OutlinedTextField(
                        value = mobile,
                        onValueChange = { mobile = it },
                        label = { Text("Mobile Number (OTP Simulated)", color = SlateGray) },
                        leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = ElectricMint) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = IcyWhite,
                            unfocusedTextColor = IcyWhite,
                            focusedBorderColor = ElectricMint,
                            unfocusedBorderColor = DeepGray
                        ),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
                            .testTag("phone_input"),
                        shape = RoundedCornerShape(12.dp)
                    )

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email Address", color = SlateGray) },
                        leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = ElectricMint) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = IcyWhite,
                            unfocusedTextColor = IcyWhite,
                            focusedBorderColor = ElectricMint,
                            unfocusedBorderColor = DeepGray
                        ),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
                            .testTag("email_input"),
                        shape = RoundedCornerShape(12.dp)
                    )

                    // Optional Referral Code & Female Check
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = isFemale,
                            onCheckedChange = { isFemale = it },
                            colors = CheckboxDefaults.colors(
                                checkedColor = ElectricMint,
                                uncheckedColor = SlateGray
                            ),
                            modifier = Modifier.testTag("gender_checkbox")
                        )
                        Text(
                            text = "Female Gender (Enables Women Mode Matchmaking)",
                            color = IcyWhite,
                            fontSize = 11.sp,
                            modifier = Modifier.clickable { isFemale = !isFemale }
                        )
                    }

                    OutlinedTextField(
                        value = referCode,
                        onValueChange = { referCode = it },
                        label = { Text("Referral Promo Code (Optional)", color = SlateGray) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = IcyWhite,
                            unfocusedTextColor = IcyWhite,
                            focusedBorderColor = ElectricMint,
                            unfocusedBorderColor = DeepGray
                        ),
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 24.dp)
                            .testTag("referral_input"),
                        shape = RoundedCornerShape(12.dp)
                    )

                    // Login Action Button
                    Button(
                        onClick = {
                            viewModel.handleLogin(mobile, name, email, role, isFemale, referCode)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = ElectricMint),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("login_button"),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text(
                            text = "START ZERO EMISSION TRIP",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = MidnightBlack
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Eco Footer Stats
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Co2,
                    contentDescription = null,
                    tint = VoltYellow,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Bengaluru EV Mobility: 48,930 kg CO2 saved today!",
                    fontSize = 11.sp,
                    color = SlateGray,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
