package com.example.myapplication.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ProfileScreen() {
    val primaryColor = Color(0xFF1B3C53)

    // ---------- State Variables ----------
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var fullName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var dob by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var homeAddress by remember { mutableStateOf("") }
    var workAddress by remember { mutableStateOf("") }
    var bloodGroup by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var medicalConditions by remember { mutableStateOf("") }
    var emergencyMessage by remember { mutableStateOf("") }
    var sendHealthInfo by remember { mutableStateOf(false) }
    var shareWithHospital by remember { mutableStateOf(false) }

    val personalFields = listOf(
        "Full Name" to fullName,
        "Phone Number" to phoneNumber,
        "Date of Birth" to dob,
        "Gender" to gender,
        "Home Address" to homeAddress,
        "Work Address" to workAddress
    )

    val healthFields = listOf(
        "Blood Group" to bloodGroup,
        "Height" to height,
        "Weight" to weight,
        "Medical Conditions" to medicalConditions
    )

    val fieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = primaryColor,
        unfocusedBorderColor = Color.Gray,
        cursorColor = primaryColor,
        focusedLabelColor = primaryColor,
        unfocusedLabelColor = primaryColor
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6F6F6))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // ---------- Heading like Dashboard ----------
        item {
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "Profile",
                    tint = primaryColor,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Profile & Health Info",
                    fontSize = 30.sp,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                    color = primaryColor
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // ---------- Profile Picture ----------
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier.size(120.dp),
                        contentAlignment = Alignment.BottomEnd
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "Profile Placeholder",
                            tint = primaryColor,
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape)
                        )
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = "Edit Picture",
                            tint = Color.White,
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(primaryColor)
                                .padding(6.dp)
                                .clickable { /* Pick image later */ }
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    OutlinedTextField(
                        value = username,
                        onValueChange = { username = it },
                        label = { Text("Username") },
                        colors = fieldColors,
                        textStyle = androidx.compose.ui.text.TextStyle(color = primaryColor),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") },
                        colors = fieldColors,
                        textStyle = androidx.compose.ui.text.TextStyle(color = primaryColor),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        // ---------- Personal Info ----------
        item {
            InfoCard(title = "Personal Info", fields = personalFields, fieldColors = fieldColors) { label, value ->
                when (label) {
                    "Full Name" -> fullName = value
                    "Phone Number" -> phoneNumber = value
                    "Date of Birth" -> dob = value
                    "Gender" -> gender = value
                    "Home Address" -> homeAddress = value
                    "Work Address" -> workAddress = value
                }
            }
        }

        // ---------- Health Info ----------
        item {
            InfoCard(title = "Health Info", fields = healthFields, fieldColors = fieldColors) { label, value ->
                when (label) {
                    "Blood Group" -> bloodGroup = value
                    "Height" -> height = value
                    "Weight" -> weight = value
                    "Medical Conditions" -> medicalConditions = value
                }
            }
        }

        // ---------- Emergency Preferences ----------
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(6.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Emergency Preferences",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = primaryColor
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = emergencyMessage,
                        onValueChange = { emergencyMessage = it },
                        label = { Text("Default Emergency Message") },
                        colors = fieldColors,
                        textStyle = androidx.compose.ui.text.TextStyle(color = primaryColor),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Send Health Info in Alert", modifier = Modifier.weight(1f))
                        Switch(
                            checked = sendHealthInfo,
                            onCheckedChange = { sendHealthInfo = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = primaryColor)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Share Data with Nearby Hospital", modifier = Modifier.weight(1f))
                        Switch(
                            checked = shareWithHospital,
                            onCheckedChange = { shareWithHospital = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = primaryColor)
                        )
                    }
                }
            }
        }

        // ---------- Buttons ----------
        item {
            Button(
                onClick = { /* Save changes */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Save Changes", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = { /* Logout */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Logout", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}


@Composable
fun InfoCard(
    title: String,
    fields: List<Pair<String, String>>,
    fieldColors: TextFieldColors,
    onValueChange: (label: String, value: String) -> Unit
) {
    val primaryColor = Color(0xFF1B3C53)
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = title,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = primaryColor
            )
            Spacer(modifier = Modifier.height(12.dp))
            fields.forEach { (label, value) ->
                OutlinedTextField(
                    value = value,
                    onValueChange = { onValueChange(label, it) },
                    label = { Text(label) },
                    keyboardOptions = if (label == "Phone Number") KeyboardOptions(keyboardType = KeyboardType.Phone) else KeyboardOptions.Default,
                    colors = fieldColors,
                    textStyle = TextStyle(color = primaryColor),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}

