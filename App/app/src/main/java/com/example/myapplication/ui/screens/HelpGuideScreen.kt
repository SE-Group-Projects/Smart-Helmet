package com.example.myapplication.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Help
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HelpGuideScreen(onBack: () -> Unit) {
    val primaryColor = Color(0xFF1B3C53)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 16.dp) // same horizontal padding as other pages
    ) {
        Spacer(modifier = Modifier.height(24.dp)) // consistent top space

        // Page Heading with Icon
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Default.Help, // use Help icon
                contentDescription = "Help & User Guide",
                tint = Color(0xFF1B3C53),
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Help & User Guide",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1B3C53)
            )
        }

        Spacer(modifier = Modifier.height(24.dp)) // space after heading


        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Section 1: Getting Started
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Getting Started",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = primaryColor
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "1. Register/Login to access Smart Helmet features.\n" +
                                "2. Update your profile with personal details.\n" +
                                "3. Add emergency contacts to enable alerts.",
                        fontSize = 15.sp,
                        color = Color.Gray
                    )
                }
            }

            // Section 2: Main Features
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Main Features",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = primaryColor
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "- Wear Detection: Ensures the helmet is worn before riding.\n" +
                                "- GPS Tracking: Monitors location in real-time.\n" +
                                "- Emergency Alerts: Sends SMS during accidents.\n" +
                                "- Speed Alerts: Warns when exceeding limits.\n" +
                                "- Temperature Control: Adjusts comfort automatically.",
                        fontSize = 15.sp,
                        color = Color.Gray
                    )
                }
            }

            // Section 3: Help & Support
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Help & Support",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = primaryColor
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "• Ensure internet, GPS, and Bluetooth are enabled.\n" +
                                "• Confirm emergency contacts are saved.\n" +
                                "• For further support, email: support@smarthelmet.com",
                        fontSize = 15.sp,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}
