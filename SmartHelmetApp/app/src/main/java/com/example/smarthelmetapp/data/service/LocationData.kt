package com.example.smarthelmetapp.data.service


import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.*
import kotlin.coroutines.resume

data class LocationData(
    val latitude: Double,
    val longitude: Double,
    val locationName: String,
    val speed: Float = 0f,
    val timestamp: Long = System.currentTimeMillis()
)

class LocationService(private val context: Context) {
    private val TAG = "LocationService"
    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)
    private val geocoder = Geocoder(context, Locale.getDefault())

    fun getLocationUpdates(): Flow<LocationData> = callbackFlow {
        if (!hasLocationPermission()) {
            Log.e(TAG, "Location permission not granted")
            close()
            return@callbackFlow
        }

        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            5000L // Update every 5 seconds
        ).apply {
            setMinUpdateIntervalMillis(2000L)
            setMaxUpdateDelayMillis(10000L)
        }.build()

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                result.lastLocation?.let { location ->
                    val locationName = getLocationName(location.latitude, location.longitude)
                    val locationData = LocationData(
                        latitude = location.latitude,
                        longitude = location.longitude,
                        locationName = locationName,
                        speed = location.speed * 3.6f, // Convert m/s to km/h
                        timestamp = System.currentTimeMillis()
                    )
                    trySend(locationData)
                }
            }
        }

        try {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        } catch (e: SecurityException) {
            Log.e(TAG, "Location permission error: ${e.message}")
            close(e)
        }

        awaitClose {
            fusedLocationClient.removeLocationUpdates(locationCallback)
        }
    }

    suspend fun getLastKnownLocation(): LocationData? = suspendCancellableCoroutine { continuation ->
        if (!hasLocationPermission()) {
            continuation.resume(null)
            return@suspendCancellableCoroutine
        }

        try {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    if (location != null) {
                        val locationName = getLocationName(location.latitude, location.longitude)
                        continuation.resume(
                            LocationData(
                                latitude = location.latitude,
                                longitude = location.longitude,
                                locationName = locationName,
                                speed = location.speed * 3.6f
                            )
                        )
                    } else {
                        continuation.resume(null)
                    }
                }
                .addOnFailureListener {
                    Log.e(TAG, "Failed to get last location: ${it.message}")
                    continuation.resume(null)
                }
        } catch (e: SecurityException) {
            Log.e(TAG, "Location permission error: ${e.message}")
            continuation.resume(null)
        }
    }

    private fun getLocationName(latitude: Double, longitude: Double): String {
        return try {
            val addresses = geocoder.getFromLocation(latitude, longitude, 1)
            if (!addresses.isNullOrEmpty()) {
                val address = addresses[0]
                val cityName = address.locality ?: address.subAdminArea
                val countryName = address.countryName

                when {
                    cityName != null && countryName != null -> "$cityName, $countryName"
                    cityName != null -> cityName
                    countryName != null -> countryName
                    else -> "Unknown Location"
                }
            } else {
                "Unknown Location"
            }
        } catch (e: Exception) {
            Log.e(TAG, "Geocoding failed: ${e.message}")
            "Unknown Location"
        }
    }

    private fun hasLocationPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        const val REQUIRED_PERMISSIONS = Manifest.permission.ACCESS_FINE_LOCATION
    }
}