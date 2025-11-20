package com.example.myapplication.ui.auth


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth

@Composable
fun HomeScreen(navController: NavController) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Smart Helmet Dashboard", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))

            // Example helmet data
            Text("Temperature: 36.5°C")
            Text("Helmet Status: Worn ✅")
            Text("GPS Location: 7.2906° N, 80.6337° E")
            Text("Collision Alerts: None")

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                FirebaseAuth.getInstance().signOut()
                navController.navigate("login") {
                    popUpTo("home") { inclusive = true } // prevent back navigation
                }
            }) {
                Text("Logout")
            }
        }
    }
}