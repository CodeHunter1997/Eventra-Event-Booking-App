package com.example.eventapp.Organizer.Authentication

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class OrganizerSignUpViewModel : ViewModel() {

    val companyName = MutableLiveData<String>()
    val email = MutableLiveData<String>()
    val phoneNumber = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val confirmPassword = MutableLiveData<String>()
}