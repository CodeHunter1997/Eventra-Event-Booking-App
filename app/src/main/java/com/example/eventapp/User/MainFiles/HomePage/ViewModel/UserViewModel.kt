package com.example.eventapp.User.MainFiles.HomePage.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.eventapp.Auth.DataModels.AttendeeData

class UserViewModel : ViewModel() {
    val userData = MutableLiveData<AttendeeData>()

    fun setUserData(data: AttendeeData) {
        userData.value = data
    }
}