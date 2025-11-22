package com.example.myapplication.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Canvas
import androidx.compose.ui.graphics.drawscope.Stroke

@Composable
fun TemperatureScreen() {
    var ventOpened by remember { mutableStateOf(true) }
    var temperature by remember { mutableStateOf(32f) } // demo temperature
    val maxTemperature = 50f
    val temperatureProgress = temperature / maxTemperature
    val animatedProgress by animateFloatAsState(targetValue = temperatureProgress)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6F8FA))
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        // Heading
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Default.Thermostat,
                contentDescription = "Temperature",
                tint = Color(0xFF1B3C53),
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Temperature",
                fontSize = 34.sp,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                color = Color(0xFF1B3C53)
            )
        }

        Spacer(modifier = Modifier.height(32.dp)) // move cards slightly down

        // Big Card for Temperature
        Card(
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp) // slightly bigger
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                // 3/4th Circular Progress Bar
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.size(160.dp) // bigger
                ) {
                    // Background arc
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        drawArc(
                            color = Color(0xFFD9E1EB),
                            startAngle = 135f,
                            sweepAngle = 270f,
                            useCenter = false,
                            style = Stroke(width = 16f) // bolder stroke
                        )
                    }

                    // Animated progress arc
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        drawArc(
                            color = Color(0xFF1B3C53),
                            startAngle = 135f,
                            sweepAngle = 270f * animatedProgress,
                            useCenter = false,
                            style = Stroke(width = 16f) // bolder stroke
                        )
                    }

                    Text(
                        text = "${temperature.toInt()}°C",
                        fontSize = 32.sp, // bigger value
                        color = Color(0xFF1B3C53),
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                    )
                }

                // Vent Status Card inside Temperature Card
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF6F8FA)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(90.dp) // bigger card
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Air,
                                contentDescription = "Vent Icon",
                                tint = Color(0xFF1B3C53),
                                modifier = Modifier.size(32.dp) // bigger icon
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Vent Status",
                                    fontSize = 18.sp, // bigger text
                                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                                    color = Color(0xFF1B3C53)
                                )
                                Text(
                                    text = if (ventOpened) "Opened" else "Closed",
                                    fontSize = 16.sp, // bigger
                                    color = Color(0xFF547792)
                                )
                            }
                        }
                        Switch(
                            checked = ventOpened,
                            onCheckedChange = { ventOpened = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color(0xFF1B3C53),
                                checkedTrackColor = Color(0xFF45657C)
                            )
                        )
                    }
                }
            }
        }
    }
}
