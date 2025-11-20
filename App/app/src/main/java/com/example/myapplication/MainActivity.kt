package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.ui.auth.HomeScreen
import com.example.myapplication.ui.auth.LoginScreen
import com.example.myapplication.ui.auth.RegisterScreen
import com.example.myapplication.viewmodel.AuthViewModel
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        enableEdgeToEdge()

        setContent {
            App()
        }
    }
}

@Composable
fun App() {
    MaterialTheme {
        Surface {
            AppNav()
        }
    }
}

@Composable
fun AppNav() {
    val navController = rememberNavController()

    // Shared ViewModel for both login and register
    val viewModel = AuthViewModel()

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {

        composable("login") {
            LoginScreen(
                viewModel = viewModel,
                onLoginSuccess = {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true } // prevents back navigation to login
                    }
                },
                onNavigateToRegister = { navController.navigate("register") }
            )
        }

        composable("register") {
            RegisterScreen(
                viewModel = viewModel,
                onRegisterSuccess = {
                    navController.navigate("home") {
                        popUpTo("register") { inclusive = true } // prevents back navigation to register
                    }
                },
                onNavigateToLogin = { navController.popBackStack() }
            )
        }

        composable("home") {
            HomeScreen(navController = navController) // pass NavController for logout
        }
    }
}
