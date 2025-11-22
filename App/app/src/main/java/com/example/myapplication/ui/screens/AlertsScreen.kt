package com.example.myapplication.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight

data class Alert(
    val type: String,
    val location: String,
    val speed: String? = null,
    val time: String
)

@Composable
fun AlertsScreen() {
    // Demo alerts
    val alerts = remember {
        listOf(
            Alert("Speed Alert", "Colombo", "65 km/h", "10:25 AM"),
            Alert("Collision Alert", "Galle", null, "09:15 AM"),
            Alert("Speed Alert", "Kandy", "80 km/h", "08:50 AM")
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6F8FA))
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        // Heading like Dashboard / Location (centered)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Warning,
                contentDescription = "Alerts",
                tint = Color(0xFF1B3C53),
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Alerts",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1B3C53)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Alerts List
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            alerts.forEach { alert ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        // Icon + Alert Heading
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val alertIcon = when (alert.type) {
                                "Speed Alert" -> Icons.Default.Speed
                                "Collision Alert" -> Icons.Default.Warning
                                else -> Icons.Default.Warning
                            }

                            Icon(
                                imageVector = alertIcon,
                                contentDescription = alert.type,
                                tint = Color(0xFF1B3C53),
                                modifier = Modifier.size(28.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = alert.type,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1B3C53)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Location + Time + Speed
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = "Location",
                                tint = Color(0xFF547792),
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = alert.location,
                                fontSize = 16.sp,
                                color = Color(0xFF547792)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = alert.time,
                                fontSize = 14.sp,
                                color = Color(0xFF547792)
                            )
                        }

                        // Speed (only if applicable)
                        alert.speed?.let {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = it,
                                fontSize = 16.sp,
                                color = Color(0xFF547792)
                            )
                        }
                    }
                }
            }
        }
    }
}
