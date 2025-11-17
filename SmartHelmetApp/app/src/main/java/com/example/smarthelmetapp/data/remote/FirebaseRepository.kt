package com.example.smarthelmetapp.data.remote

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.example.smarthelmetapp.data.local.entity.*

class FirebaseRepository {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    // ------------------- USERS -------------------
    fun saveUserProfile(user: UserProfile) {
        db.collection("users")
            .document(user.userId)
            .set(user)
    }

    fun getCurrentUserId(): String? = auth.currentUser?.uid

    // ------------------- EMERGENCY CONTACTS -------------------
    fun addEmergencyContact(userId: String, contact: EmergencyContact) {
        db.collection("users")
            .document(userId)
            .collection("emergency_contacts")
            .add(contact)
    }

    // ------------------- HELMET DATA -------------------
    fun uploadHelmetData(data: HelmetData) {
        db.collection("helmet_data")
            .add(data)
    }

    // ------------------- ALERTS -------------------
    fun uploadAlert(alert: Alert) {
        db.collection("alerts")
            .add(alert)
    }
}
