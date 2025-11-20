package com.example.myapplication.ui.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// 1️⃣ Dashboard state data class
data class DashboardState(
    val isLoading: Boolean = true,
    val helmetStatus: String = "Unknown",
    val batteryLevel: Float = 0.5f,
    val speed: String = "0 km/h",
    val temperature: String = "25 °C",
    val location: String = "Unknown",
    val isHelmetConnected: Boolean = false
)

// 2️⃣ Helmet ViewModel class
class HelmetViewModel : ViewModel() {

    private val _dashboardState = MutableStateFlow(DashboardState())
    val dashboardState: StateFlow<DashboardState> = _dashboardState.asStateFlow()

    fun updateHelmetData(
        helmetStatus: String,
        batteryLevel: Float,
        speed: String,
        temperature: String,
        location: String,
        isConnected: Boolean
    ) {
        _dashboardState.value = _dashboardState.value.copy(
            helmetStatus = helmetStatus,
            batteryLevel = batteryLevel,
            speed = speed,
            temperature = temperature,
            location = location,
            isHelmetConnected = isConnected,
            isLoading = false
        )
    }
}
