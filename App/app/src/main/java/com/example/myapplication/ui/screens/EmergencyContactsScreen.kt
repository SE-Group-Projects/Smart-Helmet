package com.example.myapplication.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Contacts
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight

@Composable
fun EmergencyContactsScreen(
    onAddContactClick: () -> Unit
) {
    val primaryColor = Color(0xFF1B3C53) // same color used for headings

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6F8FA))
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp)) // same top space as other pages

        // Page Heading with Icon
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Default.Contacts, // heading icon
                contentDescription = "Emergency Contacts",
                tint = Color(0xFF1B3C53),
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Emergency Contacts",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1B3C53)
            )
        }

        Spacer(modifier = Modifier.height(24.dp)) // space after heading


        // Section 1: Add Contacts
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
                    text = "Contacts who will be notified during emergencies",
                    fontSize = 16.sp,
                    color = primaryColor // changed to blue
                )
                Spacer(modifier = Modifier.height(12.dp))

                // Animated button
                var pressed by remember { mutableStateOf(false) }
                val scale by animateFloatAsState(
                    targetValue = if (pressed) 0.95f else 1f,
                    animationSpec = tween(durationMillis = 100)
                )

                Button(
                    onClick = { onAddContactClick() },
                    colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .scale(scale)
                        .clickable(
                            onClick = {
                                pressed = true
                                onAddContactClick()
                            },
                            onClickLabel = "Add Contact"
                        )
                ) {
                    Text(text = "Add Contact", color = Color.White, fontWeight = FontWeight.Medium)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Section 2: Emergency Contacts List
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
                    text = "Emergency Contacts List",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = primaryColor
                )
                Spacer(modifier = Modifier.height(12.dp))

                // Placeholder contacts
                EmergencyContactItem(name = "John Doe", number = "0771234567", relation = "Father")
                EmergencyContactItem(name = "Jane Doe", number = "0777654321", relation = "Mother")
            }
        }
    }
}

@Composable
fun EmergencyContactItem(name: String, number: String, relation: String) {
    val phoneColor = Color(0xFF1B3C53) // blue color for phone number

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(text = "$name ($relation)", fontSize = 16.sp, fontWeight = FontWeight.Medium)
        Text(text = number, fontSize = 14.sp, color = phoneColor) // phone number blue
        Divider(modifier = Modifier.padding(vertical = 8.dp))
    }
}

