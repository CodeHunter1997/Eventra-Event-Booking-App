package com.example.eventapp.User.Authentication

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class SignUpViewModel : ViewModel() {
    val firstName = MutableLiveData<String>()
    val lastName = MutableLiveData<String>()
    val email = MutableLiveData<String>()
    val phoneNumber = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val confirmPassword = MutableLiveData<String>()
    val gender = MutableLiveData<String>()
    val dob = MutableLiveData<String>()


}