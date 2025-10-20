package com.example.smarthelmetapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications

// Data class for alerts
data class AlertItem(
    val type: String, // "Collision" or "Speed"
    val time: String,
    val location: String,
    val description: String
)

@Composable
fun AlertsScreen() {
    // Initialize alerts only once
    val alerts = remember {
        listOf(
            AlertItem("Collision", "15:30", "Colombo, Main Rd", "Possible collision detected!"),
            AlertItem("Speed", "15:35", "Kandy Rd", "Speed exceeded 60 km/h"),
            AlertItem("Collision", "16:05", "Galle Face", "Sudden impact detected!"),
            AlertItem("Speed", "16:15", "Highway", "Speed exceeded 80 km/h")
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6F8FA))
            .padding(16.dp)
    ) {
        // ---------- Page Heading with Notifications icon ----------
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Notifications, // Theme-matched alert icon
                contentDescription = "Alerts",
                tint = Color(0xFF1B3C53), // Primary theme color
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Alerts Center",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1B3C53)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // ---------- Alerts List ----------
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(alerts) { alert ->
                AlertCard(alert)
            }
        }
    }
}

@Composable
fun AlertCard(alert: AlertItem) {
    // Dashboard theme: white card with subtle primary/secondary colors
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
            // ---------- Alert Type Row ----------
            Row(verticalAlignment = Alignment.CenterVertically) {
                val icon = when (alert.type) {
                    "Collision" -> Icons.Default.Warning
                    "Speed" -> Icons.Default.Speed
                    else -> Icons.Default.Warning
                }

                Icon(
                    imageVector = icon,
                    contentDescription = alert.type,
                    tint = Color(0xFF1B3C53), // Primary theme color
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "${alert.type} Alert",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1B3C53)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // ---------- Location Row ----------
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "Location",
                    tint = Color(0xFF547792), // Secondary theme color
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = alert.location,
                    fontSize = 16.sp,
                    color = Color(0xFF547792)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // ---------- Time ----------
            Text(
                text = "Time: ${alert.time}",
                fontSize = 14.sp,
                color = Color(0xFF547792)
            )

            Spacer(modifier = Modifier.height(4.dp))

            // ---------- Description ----------
            Text(
                text = alert.description,
                fontSize = 14.sp,
                color = Color(0xFF547792)
            )
        }
    }
}
