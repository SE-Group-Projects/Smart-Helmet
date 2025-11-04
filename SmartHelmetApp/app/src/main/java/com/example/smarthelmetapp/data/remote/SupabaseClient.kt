package com.example.smarthelmetapp.data.remote

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.storage.Storage
import io.github.jan.supabase.gotrue.Auth

object SupabaseClientInstance {
    private const val SUPABASE_URL = "https://jcmmbuguarnhpwtcmtmg.supabase.co"
    private const val SUPABASE_ANON_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImpjbW1idWd1YXJuaHB3dGNtdG1nIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NjExMTkwMjQsImV4cCI6MjA3NjY5NTAyNH0.aLly51UlJXqMRTi924ZKPz1VK1GbDjNJTfrOG6JOoaM"

    val client: SupabaseClient = createSupabaseClient(
        supabaseUrl = SUPABASE_URL,
        supabaseKey = SUPABASE_ANON_KEY
    ) {
        install(Auth)
        install(Postgrest)
        install(Storage)
        install(Realtime)
    }
}