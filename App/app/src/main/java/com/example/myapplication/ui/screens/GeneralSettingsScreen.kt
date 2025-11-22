package com.example.myapplication.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Settings

@Composable
fun GeneralSettingsScreen(
    onBack: () -> Unit,
    onHelpGuideClick: () -> Unit
) {
    val primaryColor = Color(0xFF1B3C53)

    var emergencySMS by remember { mutableStateOf(true) }
    var speedAlerts by remember { mutableStateOf(true) }
    var cloudSync by remember { mutableStateOf(false) }
    var darkMode by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 16.dp) // consistent with other pages
    ) {
        Spacer(modifier = Modifier.height(24.dp)) // consistent top space

        // Page Heading with Icon
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Default.Settings, // use Settings icon
                contentDescription = "General Settings",
                tint = Color(0xFF1B3C53),
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "General Settings",
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
            // Notifications & Alerts
            SettingsCard(title = "Notifications & Alerts") {
                SettingSwitchWithIcon(
                    label = "Emergency SMS Alerts",
                    icon = Icons.Default.Notifications,
                    checked = emergencySMS,
                    onCheckedChange = { emergencySMS = it }
                )
                SettingSwitchWithIcon(
                    label = "Speed Alerts",
                    icon = Icons.Default.Speed,
                    checked = speedAlerts,
                    onCheckedChange = { speedAlerts = it }
                )
            }

            // App Preferences
            SettingsCard(title = "App Preferences") {
                SettingSwitchWithIcon(
                    label = "Dark Mode",
                    icon = Icons.Default.DarkMode,
                    checked = darkMode,
                    onCheckedChange = { darkMode = it }
                )
            }

            // Data & Privacy
            SettingsCard(title = "Data & Privacy") {
                SettingSwitchWithIcon(
                    label = "Cloud Sync",
                    icon = Icons.Default.Cloud,
                    checked = cloudSync,
                    onCheckedChange = { cloudSync = it },
                    description = "Sync your helmet data securely with cloud storage."
                )
            }

            // Support & Info
            SettingsCard(title = "Support & Info") {
                InfoRow(label = "App Version", value = "1.0.0")
                ClickableInfoRow(label = "Help & FAQ", onClick = { onHelpGuideClick() })
                InfoRow(label = "Contact Support", value = "077-123-4567")
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun SettingsCard(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F8F8)),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF1B3C53) // Card title dark color
            )
            Spacer(modifier = Modifier.height(12.dp))
            content()
        }
    }
}

@Composable
fun SettingSwitchWithIcon(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    description: String? = null
) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, contentDescription = label, tint = Color(0xFF1B3C53), modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = label, fontSize = 15.sp, color = Color(0xFF333333)) // darker text
            }
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color(0xFF1B3C53),
                    checkedTrackColor = Color(0xFF3C86B9)
                )
            )
        }
        if (!description.isNullOrEmpty()) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = description, fontSize = 13.sp, color = Color(0xFF555555), modifier = Modifier.padding(start = 28.dp)) // darker gray
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, fontSize = 15.sp, color = Color(0xFF333333)) // darker text
        Text(text = value, fontSize = 15.sp, color = Color(0xFF333333))
    }
}

@Composable
fun ClickableInfoRow(label: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 15.sp,
            color = Color(0xFF1B3C53), // maintain primary color for clickable
            fontWeight = FontWeight.Medium
        )
    }
}
