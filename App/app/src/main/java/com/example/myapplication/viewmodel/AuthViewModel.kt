package com.example.myapplication.viewmodel


import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class AuthViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    val email = mutableStateOf("")
    val password = mutableStateOf("")
    val isLoading = mutableStateOf(false)
    val errorMessage = mutableStateOf("")

    // Login
    fun login(onSuccess: () -> Unit) {
        if (email.value.isBlank() || password.value.isBlank()) {
            errorMessage.value = "Please enter email and password"
            return
        }

        isLoading.value = true
        auth.signInWithEmailAndPassword(email.value, password.value)
            .addOnCompleteListener { task ->
                isLoading.value = false
                if (task.isSuccessful) {
                    onSuccess()
                } else {
                    errorMessage.value = task.exception?.message ?: "Login failed"
                }
            }
    }

    // Register
    fun register(onSuccess: () -> Unit) {
        if (email.value.isBlank() || password.value.isBlank()) {
            errorMessage.value = "Please enter email and password"
            return
        }

        isLoading.value = true
        auth.createUserWithEmailAndPassword(email.value, password.value)
            .addOnCompleteListener { task ->
                isLoading.value = false
                if (task.isSuccessful) {
                    onSuccess()
                } else {
                    errorMessage.value = task.exception?.message ?: "Registration failed"
                }
            }
    }
}