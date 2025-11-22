package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.ui.auth.LoginScreen
import com.example.myapplication.ui.auth.RegisterScreen
import com.example.myapplication.ui.screens.DashboardScreen
import com.example.myapplication.ui.screens.MainScreen
import com.example.myapplication.ui.screens.LoadingScreen
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.viewmodel.AuthViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                val navController = rememberNavController()
                AppNavHost(navController = navController)
            }
        }
    }
}

@Composable
fun AppNavHost(navController: NavHostController) {
    // Create a single shared ViewModel instance
    val authViewModel: AuthViewModel = viewModel()

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
        composable("login") {
            LoginScreen(
                viewModel = authViewModel,
                onLoginSuccess = {
                    navController.navigate("main") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onNavigateToRegister = { navController.navigate("register") }
            )
        }

        // Register Screen
        composable("register") {
            RegisterScreen(
                viewModel = authViewModel,
                onRegisterSuccess = {
                    navController.navigate("main") {
                        popUpTo("register") { inclusive = true }
                    }
                },
                onNavigateToLogin = { navController.popBackStack() }
            )

        }

        // Home / Dashboard Screen
        composable("home") {
            DashboardScreen()
        }

        // Main Screen (contains dashboard + bottom nav)
        composable("main") {
            MainScreen()
        }
    }
}
