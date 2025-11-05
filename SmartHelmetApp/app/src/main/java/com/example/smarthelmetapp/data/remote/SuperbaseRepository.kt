package com.example.smarthelmetapp.data.remote


import android.util.Log
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.Google
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SupabaseRepository {
    private val client = SupabaseClient.client
    private val TAG = "SupabaseRepository"

    // Authentication
    suspend fun signIn(email: String, password: String): Boolean = withContext(Dispatchers.IO) {
        try {
            client.auth.signInWith(Email) {
                this.email = email
                this.password = password
            }
            Log.d(TAG, "Sign in successful")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Sign in failed: ${e.message}")
            false
        }
    }

    suspend fun signUp(
        email: String,
        password: String,
        fullName: String,
        phone: String
    ): Boolean = withContext(Dispatchers.IO) {
        try {
            client.auth.signUpWith(Email) {
                this.email = email
                this.password = password
            }

            // Create user profile
            val userId = client.auth.currentUserOrNull()?.id ?: return@withContext false
            client.from("user_profiles").insert(
                SupabaseUserProfile(
                    user_id = userId,
                    email = email,
                    full_name = fullName,
                    phone_number = phone
                )
            )

            Log.d(TAG, "Sign up successful")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Sign up failed: ${e.message}")
            false
        }
    }

    suspend fun signInWithGoogle(idToken: String): Boolean = withContext(Dispatchers.IO) {
        try {
            client.auth.signInWith(Google) {
                this.idToken = idToken
            }

            // Check if profile exists, create if not
            val userId = client.auth.currentUserOrNull()?.id ?: return@withContext false
            val userEmail = client.auth.currentUserOrNull()?.email ?: ""

            val existingProfile = try {
                client.from("user_profiles")
                    .select { filter { eq("user_id", userId) } }
                    .decodeSingleOrNull<SupabaseUserProfile>()
            } catch (e: Exception) {
                null
            }

            if (existingProfile == null) {
                client.from("user_profiles").insert(
                    SupabaseUserProfile(
                        user_id = userId,
                        email = userEmail,
                        full_name = userEmail.substringBefore("@"),
                        phone_number = ""
                    )
                )
            }

            Log.d(TAG, "Google sign in successful")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Google sign in failed: ${e.message}")
            false
        }
    }

    suspend fun signOut(): Boolean = withContext(Dispatchers.IO) {
        try {
            client.auth.signOut()
            Log.d(TAG, "Sign out successful")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Sign out failed: ${e.message}")
            false
        }
    }

    fun getCurrentUserId(): String? {
        return client.auth.currentUserOrNull()?.id
    }

    fun isUserLoggedIn(): Boolean {
        return client.auth.currentUserOrNull() != null
    }

    // Helmet Data
    suspend fun syncHelmetData(data: SupabaseHelmetData): Boolean = withContext(Dispatchers.IO) {
        try {
            client.from("helmet_data").insert(data)
            Log.d(TAG, "Helmet data synced successfully")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Failed to sync helmet data: ${e.message}")
            false
        }
    }

    suspend fun getLatestHelmetData(userId: String): SupabaseHelmetData? = withContext(Dispatchers.IO) {
        try {
            client.from("helmet_data")
                .select {
                    filter {
                        eq("user_id", userId)
                    }
                    order("created_at", ascending = false)
                    limit(1)
                }
                .decodeSingleOrNull<SupabaseHelmetData>()
        } catch (e: Exception) {
            Log.e(TAG, "Failed to get helmet data: ${e.message}")
            null
        }
    }

    // Alerts
    suspend fun syncAlert(alert: SupabaseAlert): Boolean = withContext(Dispatchers.IO) {
        try {
            client.from("alerts").insert(alert)
            Log.d(TAG, "Alert synced successfully")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Failed to sync alert: ${e.message}")
            false
        }
    }

    suspend fun getAlerts(userId: String): List<SupabaseAlert> = withContext(Dispatchers.IO) {
        try {
            client.from("alerts")
                .select {
                    filter {
                        eq("user_id", userId)
                    }
                    order("created_at", ascending = false)
                }
                .decodeList<SupabaseAlert>()
        } catch (e: Exception) {
            Log.e(TAG, "Failed to get alerts: ${e.message}")
            emptyList()
        }
    }

    // Emergency Contacts
    suspend fun syncEmergencyContact(contact: SupabaseEmergencyContact): Boolean = withContext(Dispatchers.IO) {
        try {
            client.from("emergency_contacts").insert(contact)
            Log.d(TAG, "Emergency contact synced successfully")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Failed to sync emergency contact: ${e.message}")
            false
        }
    }

    suspend fun getEmergencyContacts(userId: String): List<SupabaseEmergencyContact> = withContext(Dispatchers.IO) {
        try {
            client.from("emergency_contacts")
                .select {
                    filter {
                        eq("user_id", userId)
                    }
                }
                .decodeList<SupabaseEmergencyContact>()
        } catch (e: Exception) {
            Log.e(TAG, "Failed to get emergency contacts: ${e.message}")
            emptyList()
        }
    }

    suspend fun deleteEmergencyContact(contactId: Int): Boolean = withContext(Dispatchers.IO) {
        try {
            client.from("emergency_contacts").delete {
                filter {
                    eq("id", contactId)
                }
            }
            Log.d(TAG, "Emergency contact deleted successfully")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Failed to delete emergency contact: ${e.message}")
            false
        }
    }

    // User Profile
    suspend fun getUserProfile(userId: String): SupabaseUserProfile? = withContext(Dispatchers.IO) {
        try {
            client.from("user_profiles")
                .select {
                    filter {
                        eq("user_id", userId)
                    }
                }
                .decodeSingleOrNull<SupabaseUserProfile>()
        } catch (e: Exception) {
            Log.e(TAG, "Failed to get user profile: ${e.message}")
            null
        }
    }

    suspend fun updateUserProfile(profile: SupabaseUserProfile): Boolean = withContext(Dispatchers.IO) {
        try {
            client.from("user_profiles").update(profile) {
                filter {
                    eq("user_id", profile.user_id)
                }
            }
            Log.d(TAG, "User profile updated successfully")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Failed to update user profile: ${e.message}")
            false
        }
    }
}