package com.example.smarthelmetapp.data.remote

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.GoTrue
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.realtime.Realtime

object SupabaseClient {
    // TODO: Replace with your Supabase URL and Key
    private const val SUPABASE_URL = "https://jcmmbuguarnhpwtcmtmg.supabase.co"
    private const val SUPABASE_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImpjbW1idWd1YXJuaHB3dGNtdG1nIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NjExMTkwMjQsImV4cCI6MjA3NjY5NTAyNH0.aLly51UlJXqMRTi924ZKPz1VK1GbDjNJTfrOG6JOoaM"

    val client: SupabaseClient = createSupabaseClient(
        supabaseUrl = SUPABASE_URL,
        supabaseKey = SUPABASE_KEY
    ) {
        install(GoTrue)
        install(Postgrest)
        install(Realtime)
    }
}

// Data classes for Supabase
data class SupabaseHelmetData(
    val id: Int? = null,
    val user_id: String,
    val temperature: Float,
    val speed: Float,
    val latitude: Double,
    val longitude: Double,
    val location_name: String,
    val battery_level: Float,
    val helmet_status: String,
    val created_at: String? = null
)

data class SupabaseAlert(
    val id: Int? = null,
    val user_id: String,
    val type: String,
    val time: String,
    val location: String,
    val latitude: Double,
    val longitude: Double,
    val description: String,
    val created_at: String? = null,
    val is_read: Boolean = false
)

data class SupabaseEmergencyContact(
    val id: Int? = null,
    val user_id: String,
    val name: String,
    val phone_number: String,
    val relationship: String,
    val created_at: String? = null
)

data class SupabaseUserProfile(
    val user_id: String,
    val email: String,
    val full_name: String,
    val phone_number: String,
    val date_of_birth: String? = null,
    val gender: String? = null,
    val home_address: String? = null,
    val work_address: String? = null,
    val blood_group: String? = null,
    val height: String? = null,
    val weight: String? = null,
    val medical_conditions: String? = null,
    val emergency_message: String? = null,
    val send_health_info: Boolean = false,
    val share_with_hospital: Boolean = false,
    val profile_image_url: String? = null,
    val updated_at: String? = null
)