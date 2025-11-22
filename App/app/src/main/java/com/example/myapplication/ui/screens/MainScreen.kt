package com.example.myapplication.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.compose.animation.core.Spring
import com.example.myapplication.R

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    // Observe the current route
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: "dashboard"

    // Determine which bottom nav item should be selected
    val selectedBottomPage = when (currentRoute) {
        "dashboard" -> "dashboard"
        "location" -> "location"
        "alerts" -> "alerts"
        "temperature" -> "temperature"
        "settings", "emergency", "add_contact", "help", "general" -> "settings"
        "profile" -> "settings" // profile is top bar only
        else -> ""
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // ------------------- Top Bar -------------------
        TopBar(
            isProfileSelected = currentRoute == "profile",
            onProfileClick = { navController.navigate("profile") }
        )

        // ------------------- Content -------------------
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            NavHost(
                navController = navController,
                startDestination = "dashboard"
            ) {
                composable("dashboard") { DashboardScreen() }
                composable("location") { LocationScreen() }
                composable("alerts") { AlertsScreen() }
                composable("temperature") { TemperatureScreen() }

                // Settings page
                composable("settings") {
                    SettingsContent(
                        onProfileClick = { navController.navigate("profile") },
                        onEmergencyContactsClick = { navController.navigate("emergency") },
                        onHelpClick = { navController.navigate("help") },
                        onGeneralClick = { navController.navigate("general") }
                    )
                }

                // Profile page
                composable("profile") { ProfileScreen() }

                // Emergency Contacts page
                composable("emergency") {
                    EmergencyContactsScreen(
                        onAddContactClick = { navController.navigate("add_contact") }
                    )
                }

                // Add Contact page
                composable("add_contact") { AddContactScreen() }


                // Help page
                composable("help") {
                    HelpGuideScreen(onBack = { navController.popBackStack() })
                }
                // General Settings
                composable("general") {
                    GeneralSettingsScreen(
                        onBack = { navController.popBackStack() },  // Back navigation
                        onHelpGuideClick = { navController.navigate("help") } // Navigate to HelpGuideScreen
                    )
                }

            }
        }

        // ------------------- Bottom Navigation -------------------
        ProfessionalBottomNav(
            selectedPage = selectedBottomPage, // fixes settings icon animation
            onSelectPage = { page ->
                navController.navigate(page) {
                    // Avoid multiple copies in back stack
                    launchSingleTop = true
                }
            }
        )
    }
}

@Composable
fun TopBar(isProfileSelected: Boolean, onProfileClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .background(
                brush = Brush.verticalGradient(
                    colorStops = arrayOf(
                        0.0f to Color(0xFF3C86B9),
                        0.28f to Color(0xFF3C86B9),
                        1.0f to Color(0xFF1B3C53)
                    )
                )
            )
            .padding(WindowInsets.statusBars.asPaddingValues())
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "App Logo",
            modifier = Modifier
                .size(200.dp)
                .align(Alignment.CenterStart)
                .offset(x = (-45).dp)
        )

        val scale by animateFloatAsState(
            targetValue = if (isProfileSelected) 1.15f else 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        )
        val offsetY by animateDpAsState(
            targetValue = if (isProfileSelected) (-6).dp else 0.dp,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        )
        val bgColor by animateColorAsState(
            targetValue = if (isProfileSelected) Color(0xFF45657C) else Color.White,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioNoBouncy,
                stiffness = Spring.StiffnessMedium
            )
        )
        val iconTint by animateColorAsState(
            targetValue = if (isProfileSelected) Color.White else Color(0xFF45657C),
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioNoBouncy,
                stiffness = Spring.StiffnessMedium
            )
        )

        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 16.dp)
                .size(50.dp)
                .scale(scale)
                .offset(y = offsetY)
                .background(bgColor, shape = CircleShape)
                .clickable { onProfileClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Profile",
                tint = iconTint,
                modifier = Modifier.size(30.dp)
            )
        }
    }
}

@Composable
fun PageContent(title: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = title, fontSize = 24.sp, color = Color.Black)
    }
}

@Composable
fun ProfessionalBottomNav(
    selectedPage: String,
    onSelectPage: (String) -> Unit
) {
    val navItems = listOf(
        "dashboard" to Icons.Default.Home,
        "location" to Icons.Default.Place,
        "alerts" to Icons.Default.Notifications,
        "temperature" to Icons.Default.DeviceThermostat,
        "settings" to Icons.Default.Settings
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(95.dp)
            .background(Color(0xFF1B3C53))
            .navigationBarsPadding(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        navItems.forEach { (page, icon) ->
            val isSelected = page == selectedPage

            val scale by animateFloatAsState(
                targetValue = if (isSelected) 1.25f else 1f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
            val offsetY by animateDpAsState(
                targetValue = if (isSelected) (-10).dp else 0.dp,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
            val bgColor by animateColorAsState(
                targetValue = if (isSelected) Color(0xFF45657C) else Color.Transparent,
                animationSpec = tween(250)
            )
            val iconTint by animateColorAsState(
                targetValue = if (isSelected) Color.White else Color.Gray,
                animationSpec = tween(250)
            )

            Box(
                modifier = Modifier
                    .offset(y = offsetY)
                    .size(55.dp)
                    .scale(scale)
                    .background(bgColor, shape = CircleShape)
                    .clickable { onSelectPage(page) },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = page,
                    tint = iconTint,
                    modifier = Modifier.size(30.dp)
                )
            }
        }
    }
}
