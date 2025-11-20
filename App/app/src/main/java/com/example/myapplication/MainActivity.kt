package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.ui.auth.*
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.ui.screens.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                val navController = rememberNavController()
                AppNavHost(navController)
            }
        }
    }
}

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "loading") {
        // Loading Screen
        composable("loading") {
            LoadingScreen(onLoadingFinished = {
                navController.navigate("login") {
                    popUpTo("loading") { inclusive = true }
                }
            })
        }

        // Login Screen
//        composable("login") {
//            LoginScreen(
//                onLoginSuccess = {
//                    navController.navigate("main") {
//                        popUpTo("login") { inclusive = true }
//                    }
//                }
//            )
//        }

        // ✅ Main Screen (contains dashboard + bottom nav)
        // Dashboard composable should be REMOVED
// Instead, after login → go to "main"

        composable("main") {
            MainScreen()
        }

    }
}
