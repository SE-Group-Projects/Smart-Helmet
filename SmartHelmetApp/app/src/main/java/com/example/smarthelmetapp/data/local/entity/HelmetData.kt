package com.example.smarthelmetapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "helmet_data")
data class HelmetData(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val temperature: Float,
    val speed: Float,
    val latitude: Double,
    val longitude: Double,
    val locationName: String,
    val batteryLevel: Float,
    val helmetStatus: String, // "Worn" or "Not Worn"
    val timestamp: Long = System.currentTimeMillis(),
    val isSynced: Boolean = false
)

@Entity(tableName = "alerts")
data class Alert(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val type: String, // "Collision", "Speed", "Temperature"
    val time: String,
    val location: String,
    val latitude: Double,
    val longitude: Double,
    val description: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isSynced: Boolean = false,
    val isRead: Boolean = false
)

@Entity(tableName = "emergency_contacts")
data class EmergencyContact(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val phoneNumber: String,
    val relationship: String,
    val userId: String,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey
    val userId: String,
    val email: String,
    val fullName: String,
    val phoneNumber: String,
    val dateOfBirth: String = "",
    val gender: String = "",
    val homeAddress: String = "",
    val workAddress: String = "",
    val bloodGroup: String = "",
    val height: String = "",
    val weight: String = "",
    val medicalConditions: String = "",
    val emergencyMessage: String = "",
    val sendHealthInfo: Boolean = false,
    val shareWithHospital: Boolean = false,
    val profileImageUrl: String = "",
    val lastUpdated: Long = System.currentTimeMillis()
)