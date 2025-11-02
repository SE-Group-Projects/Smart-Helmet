package com.example.smarthelmetapp.data.remote

import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.user.UserSession

class SupabaseRepository {
    private val client = SupabaseClientInstance.client
    suspend fun signUp(email: String, password:String, fullName:String): Boolean {
        return try {
            client.auth.signInWith( io.github.jan.supabase.gotrue.providers.builtin.Email){
                this.email = email
                this.password = password
            }
            true
        }catch (e : Exception){
            e.printStackTrace();
            false;
        }
    }

    suspend fun getCurrentSession() : UserSession? {
        return try{
            client.auth.currentSessionOrNull()
        }catch (e: Exception){
            null
        }
    }


}