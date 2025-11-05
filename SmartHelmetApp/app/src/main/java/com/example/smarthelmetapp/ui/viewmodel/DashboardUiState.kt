package com.example.smarthelmetapp.ui.viewmodel


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.smarthelmetapp.data.local.entity.*
import com.example.smarthelmetapp.data.repository.HelmetRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

data class DashboardUiState(
    val temperature: String = "0°C",
    val speed: String = "0 km/h",
    val location: String = "Unknown",
    val batteryLevel: Float = 0f,
    val helmetStatus: String = "Not Connected",
    val isLoading: Boolean = true,
    val isHelmetConnected: Boolean = false
)

data class AlertUiState(
    val alerts: List<Alert> = emptyList(),
    val isLoading: Boolean = true
)

data class ContactUiState(
    val contacts: List<EmergencyContact> = emptyList(),
    val isLoading: Boolean = true
)

data class ProfileUiState(
    val profile: UserProfile? = null,
    val isLoading: Boolean = true
)

class HelmetViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = HelmetRepository(application)

    // Dashboard State
    private val _dashboardState = MutableStateFlow(DashboardUiState())
    val dashboardState: StateFlow<DashboardUiState> = _dashboardState.asStateFlow()

    // Alerts State
    private val _alertState = MutableStateFlow(AlertUiState())
    val alertState: StateFlow<AlertUiState> = _alertState.asStateFlow()

    // Contacts State
    private val _contactState = MutableStateFlow(ContactUiState())
    val contactState: StateFlow<ContactUiState> = _contactState.asStateFlow()

    // Profile State
    private val _profileState = MutableStateFlow(ProfileUiState())
    val profileState: StateFlow<ProfileUiState> = _profileState.asStateFlow()

    // Location updates
    private val _currentLocation = MutableStateFlow<Pair<Double, Double>?>(null)
    val currentLocation: StateFlow<Pair<Double, Double>?> = _currentLocation.asStateFlow()

    init {
        observeHelmetData()
        observeAlerts()
        observeContacts()
        observeProfile()
        repository.startPeriodicSync()
    }

    private fun observeHelmetData() {
        viewModelScope.launch {
            repository.helmetData.collect { data ->
                _dashboardState.update { state ->
                    state.copy(
                        temperature = "${data.temperature.toInt()}°C",
                        speed = "${data.speed.toInt()} km/h",
                        location = data.locationName,
                        batteryLevel = data.batteryLevel,
                        helmetStatus = data.helmetStatus,
                        isLoading = false
                    )
                }

                _currentLocation.value = Pair(data.latitude, data.longitude)

                // Check for alerts
                checkForAlerts(data)
            }
        }

        viewModelScope.launch {
            repository.isHelmetConnected.collect { connected ->
                _dashboardState.update { it.copy(isHelmetConnected = connected) }
            }
        }
    }

    private fun observeAlerts() {
        viewModelScope.launch {
            repository.getAlerts().collect { alerts ->
                _alertState.update { state ->
                    state.copy(
                        alerts = alerts,
                        isLoading = false
                    )
                }
            }
        }
    }

    private fun observeContacts() {
        viewModelScope.launch {
            repository.getEmergencyContacts().collect { contacts ->
                _contactState.update { state ->
                    state.copy(
                        contacts = contacts,
                        isLoading = false
                    )
                }
            }
        }
    }

    private fun observeProfile() {
        viewModelScope.launch {
            repository.getUserProfile().collect { profile ->
                _profileState.update { state ->
                    state.copy(
                        profile = profile,
                        isLoading = false
                    )
                }
            }
        }
    }

    // Check for alert conditions
    private fun checkForAlerts(data: HelmetData) {
        viewModelScope.launch {
            val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            val currentTime = timeFormat.format(Date())

            // Speed alert (over 80 km/h)
            if (data.speed > 80) {
                val alert = Alert(
                    type = "Speed",
                    time = currentTime,
                    location = data.locationName,
                    latitude = data.latitude,
                    longitude = data.longitude,
                    description = "Speed exceeded ${data.speed.toInt()} km/h"
                )
                repository.createAlert(alert)
            }

            // Temperature alert (over 40°C)
            if (data.temperature > 40) {
                val alert = Alert(
                    type = "Temperature",
                    time = currentTime,
                    location = data.locationName,
                    latitude = data.latitude,
                    longitude = data.longitude,
                    description = "High temperature detected: ${data.temperature.toInt()}°C"
                )
                repository.createAlert(alert)
            }

            // Low battery alert (under 20%)
            if (data.batteryLevel < 0.2f && data.batteryLevel > 0) {
                val alert = Alert(
                    type = "Battery",
                    time = currentTime,
                    location = data.locationName,
                    latitude = data.latitude,
                    longitude = data.longitude,
                    description = "Low battery: ${(data.batteryLevel * 100).toInt()}%"
                )
                repository.createAlert(alert)
            }
        }
    }

    // Connect to helmet
    fun connectToHelmet(deviceAddress: String) {
        viewModelScope.launch {
            val connected = repository.connectToHelmet(deviceAddress)
            if (connected) {
                _dashboardState.update { it.copy(helmetStatus = "Connected") }
            }
        }
    }

    // Disconnect from helmet
    fun disconnectFromHelmet() {
        repository.disconnectFromHelmet()
        _dashboardState.update { it.copy(helmetStatus = "Disconnected") }
    }

    // Add emergency contact
    fun addEmergencyContact(name: String, phoneNumber: String, relationship: String) {
        viewModelScope.launch {
            val userId = repository.getCurrentUserId() ?: return@launch
            val contact = EmergencyContact(
                name = name,
                phoneNumber = phoneNumber,
                relationship = relationship,
                userId = userId
            )
            repository.addEmergencyContact(contact)
        }
    }

    // Delete emergency contact
    fun deleteEmergencyContact(contact: EmergencyContact) {
        viewModelScope.launch {
            repository.deleteEmergencyContact(contact)
        }
    }

    // Update user profile
    fun updateUserProfile(
        fullName: String,
        phoneNumber: String,
        dateOfBirth: String = "",
        gender: String = "",
        homeAddress: String = "",
        workAddress: String = "",
        bloodGroup: String = "",
        height: String = "",
        weight: String = "",
        medicalConditions: String = "",
        emergencyMessage: String = "",
        sendHealthInfo: Boolean = false,
        shareWithHospital: Boolean = false
    ) {
        viewModelScope.launch {
            val userId = repository.getCurrentUserId() ?: return@launch
            val profile = _profileState.value.profile

            val updatedProfile = UserProfile(
                userId = userId,
                email = profile?.email ?: "",
                fullName = fullName,
                phoneNumber = phoneNumber,
                dateOfBirth = dateOfBirth,
                gender = gender,
                homeAddress = homeAddress,
                workAddress = workAddress,
                bloodGroup = bloodGroup,
                height = height,
                weight = weight,
                medicalConditions = medicalConditions,
                emergencyMessage = emergencyMessage,
                sendHealthInfo = sendHealthInfo,
                shareWithHospital = shareWithHospital
            )

            repository.updateUserProfile(updatedProfile)
        }
    }

    // Authentication
    fun signIn(email: String, password: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val success = repository.signIn(email, password)
            onResult(success)
        }
    }

    fun signUp(email: String, password: String, fullName: String, phone: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val success = repository.signUp(email, password, fullName, phone)
            onResult(success)
        }
    }

    fun signInWithGoogle(idToken: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val success = repository.signInWithGoogle(idToken)
            onResult(success)
        }
    }

    fun signOut(onComplete: () -> Unit) {
        viewModelScope.launch {
            repository.signOut()
            onComplete()
        }
    }

    fun isUserLoggedIn(): Boolean = repository.isUserLoggedIn()
}