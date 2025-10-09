package com.example.smarthelmetapp.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.Switch
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Thermostat

@Composable
fun TemperatureScreen() {
    var temperature by remember { mutableStateOf(31f) } // Celsius
    var ventOpen by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6F8FA))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // ---------- Page Heading with Icon ----------
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Thermostat,
                contentDescription = "Temperature",
                tint = Color(0xFF1B3C53), // Primary color
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Temperature",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1B3C53)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ---------- Temperature Card ----------
        Card(
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(360.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                // Temperature visual with circular dial
                Box(contentAlignment = Alignment.Center) {
                    Canvas(modifier = Modifier.size(180.dp)) {
                        val stroke = Stroke(width = 20f, cap = StrokeCap.Round)
                        // Background arc
                        drawArc(
                            color = Color(0xFFE0E0E0),
                            startAngle = -210f,
                            sweepAngle = 240f,
                            useCenter = false,
                            style = stroke
                        )
                        // Foreground arc for current temperature
                        val sweep = (temperature / 50f) * 240f
                        drawArc(
                            color = Color(0xFF1B3C53),
                            startAngle = -210f,
                            sweepAngle = sweep,
                            useCenter = false,
                            style = stroke
                        )
                    }
                    Text(
                        text = "${temperature.toInt()}°C",
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1B3C53)
                    )
                }

                // ---------- Vent Status Card ----------
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Air,
                                contentDescription = "Vent Status",
                                tint = Color(0xFF1B3C53),
                                modifier = Modifier.size(36.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Vent Status",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1B3C53)
                                )
                                Text(
                                    text = if (ventOpen) "Opened" else "Closed",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (ventOpen) Color(0xFF4CAF50) else Color(0xFFF44336)
                                )
                            }
                        }

                        // Optional: Toggle switch for future backend control
                        Switch(
                            checked = ventOpen,
                            onCheckedChange = { ventOpen = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color(0xFF1B3C53),
                                uncheckedThumbColor = Color(0xFF547792)
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // ---------- Additional Info (optional) ----------
                Text(
                    text = "Temperature is stable",
                    fontSize = 14.sp,
                    color = Color(0xFF547792)
                )
            }
        }
    }
}
