package com.example.myapplication.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LocationScreen() {
    var isLiveTrackingEnabled by remember { mutableStateOf(true) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6F8FA))
            .padding(horizontal = 16.dp)
    ) {
        // Content aligned to top
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopStart) // Align top instead of centered
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Heading like Dashboard (centered horizontally)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "Location",
                    tint = Color(0xFF1B3C53),
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Location",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1B3C53)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Map placeholder
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(Color(0xFFD9E1EB), shape = RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Map Placeholder",
                    color = Color(0xFF547792),
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Current Location Card
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Current Location",
                        tint = Color(0xFF1B3C53),
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Current Location",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1B3C53)
                        )
                        Text(
                            text = "Colombo, Sri Lanka",
                            fontSize = 16.sp,
                            color = Color(0xFF547792)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Live Tracking Toggle Card
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "Live Tracking",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1B3C53)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (isLiveTrackingEnabled) "Enabled" else "Disabled",
                            fontSize = 16.sp,
                            color = Color(0xFF547792)
                        )
                    }

                    Switch(
                        checked = isLiveTrackingEnabled,
                        onCheckedChange = { isLiveTrackingEnabled = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color(0xFF1B3C53),
                            checkedTrackColor = Color(0xFF45657C)
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp)) // optional bottom spacing
        }
    }
}


