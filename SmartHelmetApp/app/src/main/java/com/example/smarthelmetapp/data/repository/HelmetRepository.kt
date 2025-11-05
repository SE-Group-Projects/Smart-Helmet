package com.example.smarthelmetapp.data.repository


import android.content.Context
import android.util.Log
import com.example.smarthelmetapp.data.local.AppDatabase
import com.example.smarthelmetapp.data.local.entity.*
import com.example.smarthelmetapp.data.remote.*
import com.example.smarthelmetapp.data.service.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class HelmetRepository(context: Context) {
    private val TAG = "HelmetRepository"

    // Local Database
    private val database = AppDatabase.getDatabase(context)
    private val helmetDataDao = database.helmetDataDao()
    private val alertDao = database.alertDao()
    private val contactDao = database.emergencyContactDao()
    private val profileDao = database.userProfileDao()

    // Remote Services
    private val supabaseRepo = SupabaseRepository()
    private val locationService = LocationService(context)
    private val weatherService = WeatherService()
    private val bluetoothService = BluetoothService(context)

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    // Connection state
    private val _isHelmetConnected = MutableStateFlow(false)
    val isHelmetConnected: StateFlow<Boolean> = _isHelmetConnected.asStateFlow()

    // Combined helmet data flow
    val helmetData: Flow<HelmetData> = flow {
        if (bluetoothService.isConnectedToDevice()) {
            // Get data from ESP32
            bluetoothService.getHelmetDataStream()
                .collect { sensorData ->
                    // Get GPS location from phone
                    val location = locationService.getLastKnownLocation()

                    val helmetData = HelmetData(
                        temperature = sensorData.temperature,
                        speed = sensorData.speed,
                        latitude = location?.latitude ?: 0.0,
                        longitude = location?.longitude ?: 0.0,
                        locationName = location?.locationName ?: "Unknown",
                        batteryLevel = sensorData.batteryLevel,
                        helmetStatus = sensorData.helmetStatus
                    )

                    // Save to local database
                    helmetDataDao.insertHelmetData(helmetData)

                    // Sync to Supabase
                    syncHelmetDataToSupabase(helmetData)

                    emit(helmetData)
                }
        } else {
            // Fallback: Use phone GPS and weather API
            locationService.getLocationUpdates()
                .collect { location ->
                    val temperature = weatherService.getTemperature(
                        location.latitude,
                        location.longitude
                    ) ?: 0f

                    val helmetData = HelmetData(
                        temperature = temperature,
                        speed = location.speed,
                        latitude = location.latitude,
                        longitude = location.longitude,
                        locationName = location.locationName,
                        batteryLevel = 0f,
                        helmetStatus = "Not Connected"
                    )

                    helmetDataDao.insertHelmetData(helmetData)
                    emit(helmetData)
                }
        }
    }

    // Connect to ESP32 helmet
    suspend fun connectToHelmet(deviceAddress: String): Boolean {
        val connected = bluetoothService.connectToDevice(deviceAddress)
        _isHelmetConnected.value = connected
        return connected
    }

    // Disconnect from helmet
    fun disconnectFromHelmet() {
        bluetoothService.disconnect()
        _isHelmetConnected.value = false
    }

    // Get latest helmet data from local DB
    fun getLatestHelmetData(): Flow<HelmetData?> {
        return helmetDataDao.getLatestHelmetData()
    }

    // Alerts
    fun getAlerts(): Flow<List<Alert>> {
        return alertDao.getAllAlerts()
    }

    suspend fun createAlert(alert: Alert) {
        alertDao.insertAlert(alert)
        syncAlertToSupabase(alert)

        // Send SMS to emergency contacts
        sendEmergencyAlerts(alert)
    }

    // Emergency Contacts
    fun getEmergencyContacts(): Flow<List<EmergencyContact>> {
        val userId = supabaseRepo.getCurrentUserId() ?: return flowOf(emptyList())
        return contactDao.getContactsByUserId(userId)
    }

    suspend fun addEmergencyContact(contact: EmergencyContact) {
        contactDao.insertContact(contact)

        // Sync to Supabase
        supabaseRepo.syncEmergencyContact(
            SupabaseEmergencyContact(
                user_id = contact.userId,
                name = contact.name,
                phone_number = contact.phoneNumber,
                relationship = contact.relationship
            )
        )
    }

    suspend fun deleteEmergencyContact(contact: EmergencyContact) {
        contactDao.deleteContact(contact)
    }

    // User Profile
    fun getUserProfile(): Flow<UserProfile?> {
        val userId = supabaseRepo.getCurrentUserId() ?: return flowOf(null)
        return profileDao.getUserProfile(userId)
    }

    suspend fun updateUserProfile(profile: UserProfile) {
        profileDao.insertProfile(profile)

        // Sync to Supabase
        supabaseRepo.updateUserProfile(
            SupabaseUserProfile(
                user_id = profile.userId,
                email = profile.email,
                full_name = profile.fullName,
                phone_number = profile.phoneNumber,
                date_of_birth = profile.dateOfBirth,
                gender = profile.gender,
                home_address = profile.homeAddress,
                work_address = profile.workAddress,
                blood_group = profile.bloodGroup,
                height = profile.height,
                weight = profile.weight,
                medical_conditions = profile.medicalConditions,
                emergency_message = profile.emergencyMessage,
                send_health_info = profile.sendHealthInfo,
                share_with_hospital = profile.shareWithHospital,
                profile_image_url = profile.profileImageUrl
            )
        )
    }

    // Authentication
    suspend fun signIn(email: String, password: String): Boolean {
        return supabaseRepo.signIn(email, password)
    }

    suspend fun signUp(email: String, password: String, fullName: String, phone: String): Boolean {
        return supabaseRepo.signUp(email, password, fullName, phone)
    }

    suspend fun signInWithGoogle(idToken: String): Boolean {
        return supabaseRepo.signInWithGoogle(idToken)
    }

    suspend fun signOut(): Boolean {
        // Clear local data
        val userId = supabaseRepo.getCurrentUserId()
        if (userId != null) {
            contactDao.deleteAllContactsForUser(userId)
            profileDao.deleteProfile(userId)
        }

        return supabaseRepo.signOut()
    }

    fun getCurrentUserId(): String? = supabaseRepo.getCurrentUserId()

    fun isUserLoggedIn(): Boolean = supabaseRepo.isUserLoggedIn()

    // Sync functions
    private suspend fun syncHelmetDataToSupabase(data: HelmetData) {
        val userId = supabaseRepo.getCurrentUserId() ?: return

        supabaseRepo.syncHelmetData(
            SupabaseHelmetData(
                user_id = userId,
                temperature = data.temperature,
                speed = data.speed,
                latitude = data.latitude,
                longitude = data.longitude,
                location_name = data.locationName,
                battery_level = data.batteryLevel,
                helmet_status = data.helmetStatus
            )
        )
    }

    private suspend fun syncAlertToSupabase(alert: Alert) {
        val userId = supabaseRepo.getCurrentUserId() ?: return

        supabaseRepo.syncAlert(
            SupabaseAlert(
                user_id = userId,
                type = alert.type,
                time = alert.time,
                location = alert.location,
                latitude = alert.latitude,
                longitude = alert.longitude,
                description = alert.description,
                is_read = alert.isRead
            )
        )
    }

    private suspend fun sendEmergencyAlerts(alert: Alert) {
        val userId = supabaseRepo.getCurrentUserId() ?: return
        val contacts = contactDao.getContactsByUserIdSync(userId)

        // TODO: Implement SMS sending logic using SmsManager
        Log.d(TAG, "Would send SMS to ${contacts.size} emergency contacts")
    }

    // Periodic sync
    fun startPeriodicSync() {
        scope.launch {
            while (true) {
                delay(60000) // Sync every minute
                syncUnsyncedData()
            }
        }
    }

    private suspend fun syncUnsyncedData() {
        try {
            // Sync unsynced helmet data
            val unsyncedData = helmetDataDao.getUnsyncedData()
            unsyncedData.forEach { data ->
                val synced = syncHelmetDataToSupabase(data)
                if (synced) {
                    helmetDataDao.markAsSynced(data.id)
                }
            }

            // Sync unsynced alerts
            val unsyncedAlerts = alertDao.getUnsyncedAlerts()
            unsyncedAlerts.forEach { alert ->
                val synced = syncAlertToSupabase(alert)
                if (synced) {
                    alertDao.markAsSynced(alert.id)
                }
            }

            Log.d(TAG, "Periodic sync completed")
        } catch (e: Exception) {
            Log.e(TAG, "Sync failed: ${e.message}")
        }
    }

    private suspend fun syncHelmetDataToSupabase(data: HelmetData): Boolean {
        val userId = supabaseRepo.getCurrentUserId() ?: return false

        return supabaseRepo.syncHelmetData(
            SupabaseHelmetData(
                user_id = userId,
                temperature = data.temperature,
                speed = data.speed,
                latitude = data.latitude,
                longitude = data.longitude,
                location_name = data.locationName,
                battery_level = data.batteryLevel,
                helmet_status = data.helmetStatus
            )
        )
    }

    private suspend fun syncAlertToSupabase(alert: Alert): Boolean {
        val userId = supabaseRepo.getCurrentUserId() ?: return false

        return supabaseRepo.syncAlert(
            SupabaseAlert(
                user_id = userId,
                type = alert.type,
                time = alert.time,
                location = alert.location,
                latitude = alert.latitude,
                longitude = alert.longitude,
                description = alert.description,
                is_read = alert.isRead
            )
        )
    }
}