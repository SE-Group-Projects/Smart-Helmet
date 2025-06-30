package com.example.smarthelmetapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel(){
    // This holds a greeting message
    private val _greeting = MutableLiveData<String>("HEllow from view model")
    val greeting: LiveData<String> = _greeting

    // You can later update LiveData when sensor ? AI outputs change...
    fun updateGreeting(message: String){
        _greeting.value = message
    }
}