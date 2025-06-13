package com.example.eventapp.Organizer.Authentication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class OrganizerLoginViewModel : ViewModel() {
    private val _email = MutableLiveData<String>()
    private val _password = MutableLiveData<String>()

    val email: LiveData<String> = _email
    val password: LiveData<String> = _password

    fun setEmail(value: String) {
        _email.value = value
    }

    fun setPassword(value: String) {
        _password.value = value
    }
}